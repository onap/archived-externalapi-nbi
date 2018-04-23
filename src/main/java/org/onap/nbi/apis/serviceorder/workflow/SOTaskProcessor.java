/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.SoClient;
import org.onap.nbi.apis.serviceorder.model.ServiceCharacteristic;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.*;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfoJson;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

@Service
public class SOTaskProcessor {

    @Value("${nbi.callForVNF}")
    private boolean enableCallForVNF;

    @Value("${onap.lcpCloudRegionId}")
    private String lcpCloudRegionId;

    @Value("${onap.tenantId}")
    private String tenantId;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    @Autowired
    private SoClient soClient;


    private static final Logger LOGGER = LoggerFactory.getLogger(SOTaskProcessor.class);

    /**
     * Run the ServiceOrchestrator processing for a serviceOrderItem which with any sub relations
     *
     * @throws InterruptedException
     */
    public void processOrderItem(ExecutionTask executionTask) throws InterruptedException {


        ServiceOrderInfoJson serviceOrderInfoJson = executionTask.getServiceOrderInfoJson();
        ServiceOrder serviceOrder = serviceOrderRepository.findOne(serviceOrderInfoJson.getServiceOrderId());
        ServiceOrderItem serviceOrderItem = null;
        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            if (item.getId().equals(executionTask.getOrderItemId())) {
                serviceOrderItem = item;
            }
        }

        ServiceOrderInfo serviceOrderInfo = null;
        try {
            serviceOrderInfo =
                    JsonEntityConverter.convertJsonToServiceOrderInfo(serviceOrderInfoJson.getServiceOrderInfoJson());
        } catch (IOException e) {
            LOGGER.warn("Unable to read ServiceOrderInfo Json for serviceOrderId " + serviceOrder.getId(), e);
        }

        if (serviceOrderItem != null && StateType.ACKNOWLEDGED == serviceOrderItem.getState()) {

            ResponseEntity<CreateServiceInstanceResponse> response = null;
            try {
                response = postSORequest(serviceOrderItem, serviceOrderInfo);
            } catch (NullPointerException e) {
                LOGGER.warn("Enable to create service instance for serviceOrderItem.id=" + serviceOrderItem.getId(), e);
                response = null;
            }

            if (response == null) {
                LOGGER.warn("response=null for serviceOrderItem.id=" + serviceOrderItem.getId());
                serviceOrderItem.setState(StateType.FAILED);
            } else {
                updateServiceOrderItem(response.getBody(), serviceOrderItem);

                if (response.getStatusCode() != HttpStatus.CREATED || response.getBody() == null
                        || response.getBody().getRequestReference() == null) {
                    serviceOrderItem.setState(StateType.FAILED);
                } else {
                    serviceOrderItem.setState(StateType.INPROGRESS);
                }
            }
        }

        if (executionTask.getNbRetries() > 0) {
            // TODO lancer en asynchrone
            pollSoRequestStatus(serviceOrderItem);
            if (serviceOrderItem.getState().equals(StateType.COMPLETED)) {
                updateSuccessTask(executionTask);
            } else {
                int nbRetries = executionTask.getNbRetries();
                executionTask.setNbRetries(--nbRetries);
                executionTask.setLastAttemptDate(new Date());
                executionTaskRepository.save(executionTask);
            }
        } else {
            updateFailedTask(executionTask, serviceOrder);
        }


        updateServiceOrder(serviceOrder);
    }

    private ResponseEntity<CreateServiceInstanceResponse> postSORequest(ServiceOrderItem serviceOrderItem,
            ServiceOrderInfo serviceOrderInfo) {
        RequestDetails requestDetails = buildSoRequest(serviceOrderItem,
                serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId()).getCatalogResponse(),
                serviceOrderInfo.getSubscriberInfo());
        MSOPayload msoPayload = new MSOPayload(requestDetails);
        ResponseEntity<CreateServiceInstanceResponse> response = null;

        switch (serviceOrderItem.getAction()) {
            case ADD:
                response = soClient.callCreateServiceInstance(msoPayload);
                break;
            case DELETE:
                response = soClient.callDeleteServiceInstance(msoPayload, serviceOrderItem.getService().getId());
                break;
            default:
                break;
        }
        return response;
    }

    private void updateServiceOrder(ServiceOrder serviceOrder) {
        boolean atLeastOneCompleted = false;
        boolean atLeastOneNotFinished = false;
        boolean atLeastOneFailed = false;


        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            switch (serviceOrderItem.getState()) {
                case COMPLETED:
                    atLeastOneCompleted = true;
                    break;
                case INPROGRESS:
                case ACKNOWLEDGED:
                    atLeastOneNotFinished = true;
                    break;
                case FAILED:
                    atLeastOneFailed = true;
                    break;
                default:
                    break;

            }
        }

        if (atLeastOneNotFinished) {
            serviceOrder.setState(StateType.INPROGRESS);
        } else {
            serviceOrder.setCompletionDateTime(new Date());
            if (atLeastOneFailed) {
                if (!atLeastOneCompleted) {
                    serviceOrder.setState(StateType.FAILED);
                } else {
                    serviceOrder.setState(StateType.PARTIAL);
                }
            } else {
                serviceOrder.setState(StateType.COMPLETED);
            }
        }
        serviceOrderRepository.save(serviceOrder);
    }


    /**
     * * @param orderItem
     *
     * @throws InterruptedException
     */
    private void pollSoRequestStatus(ServiceOrderItem orderItem) throws InterruptedException {
        boolean stopPolling = false;
        String requestId = orderItem.getRequestId();
        GetRequestStatusResponse response = null;
        int nbRetries = 0;

        while (!stopPolling) {
            response = soClient.callGetRequestStatus(requestId);
            if (response != null) {
                if (response.getRequest().getRequestStatus().getPercentProgress() != 100) {
                    nbRetries++;
                    orderItem.setState(StateType.INPROGRESS);
                    Thread.sleep(1000);
                } else if (RequestState.COMPLETE != response.getRequest().getRequestStatus().getRequestState()) {
                    orderItem.setState(StateType.FAILED);
                    stopPolling = true;
                } else {
                    orderItem.setState(StateType.COMPLETED);
                    stopPolling = true;
                }
            } else {
                orderItem.setState(StateType.INPROGRESS);
                stopPolling = true;
            }
            if (nbRetries == 3) {
                stopPolling = true;
            }
        }
    }

    /**
     * Build SO CREATE request from the ServiceOrder and catalog informations from SDC
     *
     * @param orderItem
     * @param sdcInfos
     * @param subscriberInfo
     * @return
     */
    private RequestDetails buildSoRequest(ServiceOrderItem orderItem, LinkedHashMap<String, Object> sdcInfos,
            SubscriberInfo subscriberInfo) {
        RequestDetails requestDetails = new RequestDetails();

        requestDetails.setSubscriberInfo(subscriberInfo);

        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelInvariantId((String) sdcInfos.get("invariantUUID"));
        modelInfo.setModelNameVersionId(orderItem.getService().getServiceSpecification().getId());
        modelInfo.setModelName((String) sdcInfos.get("name"));
        modelInfo.setModelVersion((String) sdcInfos.get("version"));
        requestDetails.setModelInfo(modelInfo);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setInstanceName(orderItem.getService().getName());
        requestInfo.setSource("VID");
        requestInfo.setSuppressRollback(false);
        requestDetails.setRequestInfo(requestInfo);

        RequestParameters requestParameters = new RequestParameters();
        requestParameters.setSubscriptionServiceType((String) sdcInfos.get("name"));
        requestParameters.setUserParams(
                retrieveUserParamsFromServiceCharacteristics(orderItem.getService().getServiceCharacteristic()));
        requestParameters.setaLaCarte(true);
        requestDetails.setRequestParameters(requestParameters);

        CloudConfiguration cloudConfiguration = new CloudConfiguration(lcpCloudRegionId, tenantId);
        requestDetails.setCloudConfiguration(cloudConfiguration);
        return requestDetails;
    }

    /**
     * Build a list of UserParams for the SO request by browsing a list of ServiceCharacteristics from
     * SDC
     *
     * @param characteristics
     * @return
     */
    private List<UserParams> retrieveUserParamsFromServiceCharacteristics(List<ServiceCharacteristic> characteristics) {
        List<UserParams> userParams = new ArrayList<UserParams>();

        if (!CollectionUtils.isEmpty(characteristics)) {
            for (ServiceCharacteristic characteristic : characteristics) {
                UserParams userParam = new UserParams(characteristic.getName(),
                        characteristic.getValue().getServiceCharacteristicValue());
                userParams.add(userParam);
            }
        }

        return userParams;
    }


    /**
     * Update ServiceOrderItem with SO response by using serviceOrderRepository with the serviceOrderId
     *
     * @param createServiceInstanceResponse
     * @param orderItem
     */
    private void updateServiceOrderItem(CreateServiceInstanceResponse createServiceInstanceResponse,
            ServiceOrderItem orderItem) {

        if (createServiceInstanceResponse != null && !orderItem.getState().equals(StateType.FAILED)) {
            orderItem.getService().setId(createServiceInstanceResponse.getRequestReference().getInstanceId());
            orderItem.setRequestId(createServiceInstanceResponse.getRequestReference().getRequestId());
        }
    }

    /**
     * Update an executionTask in database when it's process with a success
     *
     * @param executionTask
     */
    private void updateSuccessTask(ExecutionTask executionTask) {
        executionTaskRepository.delete(executionTask.getInternalId());
        executionTaskRepository.updateReliedTaskAfterDelete(executionTask.getInternalId());
    }

    /**
     * @param executionTask
     * @param serviceOrder
     */
    private void updateFailedTask(ExecutionTask executionTask, ServiceOrder serviceOrder) {
        List<ExecutionTask> executionTasksToDelete = findExecutionTasksRecursively(executionTask);
        for (ExecutionTask taskId : executionTasksToDelete) {
            executionTaskRepository.delete(taskId);
        }

        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            for (ExecutionTask taskToDelete : executionTasksToDelete) {
                if (taskToDelete.getOrderItemId().equals(item.getId())) {
                    item.setState(StateType.FAILED);
                }
            }
        }
    }

    /**
     * @param executionTask
     * @return
     */
    private List<ExecutionTask> findExecutionTasksRecursively(ExecutionTask executionTask) {

        List<ExecutionTask> executionTasks = new ArrayList<>();

        List<ExecutionTask> tasksReliedToAnOrderItemId =
                executionTaskRepository.findTasksReliedToAnOrderItemId(executionTask.getInternalId());

        if (CollectionUtils.isEmpty(tasksReliedToAnOrderItemId)) {
            return Arrays.asList(executionTask);
        } else {
            for (ExecutionTask task : tasksReliedToAnOrderItemId) {
                executionTasks.addAll(findExecutionTasksRecursively(task));
            }
        }
        executionTasks.add(executionTask);
        return executionTasks;
    }


}
