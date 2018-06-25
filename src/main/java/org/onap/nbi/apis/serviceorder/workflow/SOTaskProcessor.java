/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.onap.nbi.apis.serviceorder.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.onap.nbi.apis.serviceorder.SoClient;
import org.onap.nbi.apis.serviceorder.model.ServiceCharacteristic;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.CloudConfiguration;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.GetRequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.MSOPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.ModelInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestDetails;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestParameters;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestState;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.UserParams;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SOTaskProcessor {

    @Value("${nbi.callForVNF}")
    private boolean enableCallForVNF;

    @Value("${onap.lcpCloudRegionId}")
    private String lcpCloudRegionId;

    @Value("${onap.tenantId}")
    private String tenantId;


    @Autowired
    private ServiceOrderService serviceOrderService;

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

        ServiceOrderInfo serviceOrderInfo = getServiceOrderInfo(executionTask);

        ServiceOrder serviceOrder = serviceOrderService.findServiceOrderById(serviceOrderInfo.getServiceOrderId());
        ServiceOrderItem serviceOrderItem = getServiceOrderItem(executionTask, serviceOrder);

        if (StateType.ACKNOWLEDGED == serviceOrderItem.getState()) {

            ResponseEntity<CreateServiceInstanceResponse> response = postServiceOrderItem(serviceOrderInfo,
                serviceOrderItem);
            updateServiceOrderItem(response, serviceOrderItem,serviceOrder);
        }

        if (executionTask.getNbRetries() > 0 && StateType.FAILED != serviceOrderItem.getState()
            ) {
            // TODO lancer en asynchrone
            pollSoRequestStatus(serviceOrder,serviceOrderItem);
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

    private ResponseEntity<CreateServiceInstanceResponse> postServiceOrderItem(ServiceOrderInfo serviceOrderInfo,
        ServiceOrderItem serviceOrderItem) {
        ResponseEntity<CreateServiceInstanceResponse> response = null;
        try {
            response = postSORequest(serviceOrderItem, serviceOrderInfo);
        } catch (NullPointerException e) {
            LOGGER.error("Unable to create service instance for serviceOrderItem.id=" + serviceOrderItem.getId(), e);
            response = null;
        }
        return response;
    }

    private ServiceOrderItem getServiceOrderItem(ExecutionTask executionTask, ServiceOrder serviceOrder) {
        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            if (item.getId().equals(executionTask.getOrderItemId())) {
                return item;
            }
        }
        throw new TechnicalException(
            "Unable to retrieve serviceOrderItem forexecutionTaskId " + executionTask.getInternalId());
    }

    private ServiceOrderInfo getServiceOrderInfo(ExecutionTask executionTask) {
        String serviceOrderInfoJson = executionTask.getServiceOrderInfoJson();
        ServiceOrderInfo serviceOrderInfo = null;
        try {
            serviceOrderInfo =
                JsonEntityConverter.convertJsonToServiceOrderInfo(serviceOrderInfoJson);
        } catch (IOException e) {
            LOGGER
                .error("Unable to read ServiceOrderInfo Json for executionTaskId " + executionTask.getInternalId(), e);
            throw new TechnicalException(
                "Unable to read ServiceOrderInfo Json for executionTaskId " + executionTask.getInternalId());
        }
        return serviceOrderInfo;
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
            serviceOrderService.updateOrderState(serviceOrder,StateType.INPROGRESS);
        } else {
            StateType finalState;
            if (atLeastOneFailed) {
                if (!atLeastOneCompleted) {
                    finalState=StateType.FAILED;
                } else {
                    finalState=StateType.PARTIAL;
                }
            } else {
                finalState=StateType.COMPLETED;
            }
            serviceOrderService.updateOrderFinalState(serviceOrder,finalState);
        }
    }


    /**
     * * @param orderItem
     */
    private void pollSoRequestStatus(ServiceOrder serviceOrder,
        ServiceOrderItem orderItem) throws InterruptedException {
        boolean stopPolling = false;
        String requestId = orderItem.getRequestId();
        GetRequestStatusResponse response = null;
        int nbRetries = 0;

        while (!stopPolling) {
            response = soClient.callGetRequestStatus(requestId);
            if (response != null) {
                if (response.getRequest().getRequestStatus().getPercentProgress() != 100) {
                    nbRetries++;
                    serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.INPROGRESS);
                    Thread.sleep(1000);
                    LOGGER.debug("orderitem id {} still in progress from so",orderItem.getId());
                } else if (RequestState.COMPLETE != response.getRequest().getRequestStatus().getRequestState()) {
                    serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.FAILED);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} failed, response from request status {}",orderItem.getId(),response.getRequest().getRequestStatus().getRequestState());
                } else {
                    serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.COMPLETED);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} completed");
                }
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.INPROGRESS);
                stopPolling = true;
                LOGGER.debug("orderitem id {} still in progress from so",orderItem.getId());
            }
            if (nbRetries == 3) {
                stopPolling = true;
                LOGGER.debug("orderitem id {} stop polling from getrequeststatus, 3 retries done",orderItem.getId());

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
    private RequestDetails buildSoRequest(ServiceOrderItem orderItem, Map<String, Object> sdcInfos,
        SubscriberInfo subscriberInfo) {
        RequestDetails requestDetails = new RequestDetails();

        requestDetails.setSubscriberInfo(subscriberInfo);

        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelInvariantId((String) sdcInfos.get("invariantUUID"));
        modelInfo.setModelNameVersionId(orderItem.getService().getServiceSpecification().getId());
        modelInfo.setModelVersionId(orderItem.getService().getServiceSpecification().getId());
        modelInfo.setModelName((String) sdcInfos.get("name"));
        modelInfo.setModelVersion((String) sdcInfos.get("version"));
        requestDetails.setModelInfo(modelInfo);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setInstanceName(orderItem.getService().getName());
        requestInfo.setSource("VID");
        requestInfo.setSuppressRollback(false);
        requestInfo.setRequestorId("NBI");
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
     * Build a list of UserParams for the SO request by browsing a list of ServiceCharacteristics from SDC
     */
    private List<UserParams> retrieveUserParamsFromServiceCharacteristics(List<ServiceCharacteristic> characteristics) {
        List<UserParams> userParams = new ArrayList<>();

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
     *  @param response
     * @param orderItem
     * @param serviceOrder
     */
    private void updateServiceOrderItem(ResponseEntity<CreateServiceInstanceResponse> response,
        ServiceOrderItem orderItem, ServiceOrder serviceOrder) {

        if (response == null) {
            LOGGER.warn("response=null for serviceOrderItem.id=" + orderItem.getId());
            serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.FAILED);
        }
        else {
            CreateServiceInstanceResponse createServiceInstanceResponse = response.getBody();
            if (createServiceInstanceResponse != null && !orderItem.getState().equals(StateType.FAILED)) {
                orderItem.getService().setId(createServiceInstanceResponse.getRequestReferences().getInstanceId());
                orderItem.setRequestId(createServiceInstanceResponse.getRequestReferences().getRequestId());
            }

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                || response.getBody().getRequestReferences() == null) {
                serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.FAILED);
                LOGGER.warn("order item {} failed , status {} , response {}",orderItem.getId(),response.getStatusCode(),response.getBody());
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.INPROGRESS);
            }
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
            LOGGER.warn("task {} with orderitem id {} deleted cause orderitem id {} failed ",taskId.getInternalId(),taskId.getOrderItemId(),executionTask.getOrderItemId());
        }
        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            for (ExecutionTask taskToDelete : executionTasksToDelete) {
                if (taskToDelete.getOrderItemId().equals(item.getId())) {
                    serviceOrderService.updateOrderItemState(serviceOrder,item,StateType.FAILED);
                    LOGGER.warn("task {} with orderitem id {}  to failed cause orderitem id {} failed ",taskToDelete.getInternalId(),taskToDelete.getOrderItemId(),executionTask.getOrderItemId());

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
