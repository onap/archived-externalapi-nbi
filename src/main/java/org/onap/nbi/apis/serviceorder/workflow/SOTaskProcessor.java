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
import java.util.Optional;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateE2EServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateMacroServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.onap.nbi.apis.serviceorder.utils.E2EServiceUtils;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;
import org.onap.nbi.apis.serviceorder.utils.MacroServiceUtils;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SOTaskProcessor {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    @Autowired
    private PostSoProcessor postSoProcessor;

    @Autowired
    private SOGetStatusManager sOGetStatusManager;

    @Value("${scheduler.pollingDurationInMins}")
    private float pollingDurationInMins;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOTaskProcessor.class);

    /**
     * Run the ServiceOrchestrator processing for a serviceOrderItem which with any sub relations
     */
    public void processOrderItem(ExecutionTask executionTask) {

        executionTask.setLastAttemptDate(new Date());
        executionTaskRepository.save(executionTask);

        ServiceOrderInfo serviceOrderInfo = getServiceOrderInfo(executionTask);

        Optional<ServiceOrder> optionalServiceOrder =
                serviceOrderService.findServiceOrderById(serviceOrderInfo.getServiceOrderId());
        if (!optionalServiceOrder.isPresent()) {
            throw new TechnicalException(
                    "Unable to retrieve service order for id " + serviceOrderInfo.getServiceOrderId());
        }
        ServiceOrder serviceOrder = optionalServiceOrder.get();
        ServiceOrderItem serviceOrderItem = getServiceOrderItem(executionTask, serviceOrder);
        boolean e2eService =
                E2EServiceUtils.isE2EService(serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId()));
        boolean macroService = MacroServiceUtils
                .isMacroService(serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId()));

        if (shouldPostSo(serviceOrderItem)) {
            if (e2eService) {
                ResponseEntity<CreateE2EServiceInstanceResponse> response =
                        postSoProcessor.postE2EServiceOrderItem(serviceOrderInfo, serviceOrderItem, serviceOrder);
                updateE2EServiceOrderItem(response, serviceOrderItem, serviceOrder);
            } else if (macroService) {
              LOGGER.info("Mode type macro");
              // call SO macro flow.(EXTAPI-368)
              ResponseEntity<CreateMacroServiceInstanceResponse> response = postSoProcessor
                  .postMacroServiceOrderItem(serviceOrderInfo, serviceOrderItem);
              updateMacroServiceOrderItem(response, serviceOrderItem, serviceOrder);
            } else {

                ResponseEntity<CreateServiceInstanceResponse> response =
                        postSoProcessor.postServiceOrderItem(serviceOrderInfo, serviceOrderItem);
                updateServiceOrderItem(response, serviceOrderItem, serviceOrder);
            }
        }

        boolean shouldStopPolling = shouldStopPolling(executionTask);
        if (!shouldStopPolling && StateType.FAILED != serviceOrderItem.getState()) {
            // TODO lancer en asynchrone
            sOGetStatusManager.pollRequestStatus(serviceOrder, serviceOrderItem, e2eService);

            if (serviceOrderItem.getState().equals(StateType.COMPLETED)) {
                updateSuccessTask(executionTask);
            }
        } else if (shouldStopPolling && StateType.FAILED != serviceOrderItem.getState()) {
            serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "504");
            updateFailedTask(executionTask, serviceOrder);
        } else {
            updateFailedTask(executionTask, serviceOrder);
        }

        updateServiceOrder(serviceOrder);
    }

    private boolean shouldPostSo(ServiceOrderItem serviceOrderItem) {
        return StateType.ACKNOWLEDGED == serviceOrderItem.getState()
                || StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE == serviceOrderItem.getState();
    }

    private ServiceOrderItem getServiceOrderItem(ExecutionTask executionTask, ServiceOrder serviceOrder) {
        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            if (item.getId().equals(executionTask.getOrderItemId())) {
                return item;
            }
        }
        throw new TechnicalException(
                "Unable to retrieve serviceOrderItem for executionTaskId " + executionTask.getInternalId());
    }

    private ServiceOrderInfo getServiceOrderInfo(ExecutionTask executionTask) {
        String serviceOrderInfoJson = executionTask.getServiceOrderInfoJson();
        ServiceOrderInfo serviceOrderInfo = null;
        try {
            serviceOrderInfo = JsonEntityConverter.convertJsonToServiceOrderInfo(serviceOrderInfoJson);
        } catch (IOException e) {
            LOGGER.error("Unable to read ServiceOrderInfo Json for executionTaskId " + executionTask.getInternalId(),
                    e);
            throw new TechnicalException(
                    "Unable to read ServiceOrderInfo Json for executionTaskId " + executionTask.getInternalId());
        }
        return serviceOrderInfo;
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
            serviceOrderService.updateOrderState(serviceOrder, StateType.INPROGRESS);
        } else {
            StateType finalState;
            if (atLeastOneFailed) {
                if (!atLeastOneCompleted) {
                    finalState = StateType.FAILED;
                } else {
                    finalState = StateType.PARTIAL;
                }
            } else {
                finalState = StateType.COMPLETED;
            }
            serviceOrderService.updateOrderState(serviceOrder, finalState);
        }
    }

    /**
     * Update ServiceOrderItem with SO response by using serviceOrderRepository with the serviceOrderId
     */
    private void updateServiceOrderItem(ResponseEntity<CreateServiceInstanceResponse> response,
            ServiceOrderItem orderItem, ServiceOrder serviceOrder) {

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            LOGGER.warn("response ko for serviceOrderItem.id=" + orderItem.getId());
            serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
            buildOrderMessageIfNeeded(orderItem, serviceOrder, response);
        } else {
            CreateServiceInstanceResponse createServiceInstanceResponse = response.getBody();
            if (createServiceInstanceResponse != null && !orderItem.getState().equals(StateType.FAILED)) {
                orderItem.getService().setId(createServiceInstanceResponse.getRequestReferences().getInstanceId());
                orderItem.setRequestId(createServiceInstanceResponse.getRequestReferences().getRequestId());
            }

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || response.getBody().getRequestReferences() == null) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                LOGGER.warn("order item {} failed , status {} , response {}", orderItem.getId(),
                        response.getStatusCode(), response.getBody());
            } else {
                updateOrderItemToInProgress(serviceOrder, orderItem);
            }
        }
    }

    /**
     * Update ServiceOrderItem with SO response by using serviceOrderRepository with the serviceOrderId
     */
    private void updateMacroServiceOrderItem(ResponseEntity<CreateMacroServiceInstanceResponse> response,
        ServiceOrderItem orderItem, ServiceOrder serviceOrder) {

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            LOGGER.warn("response ko for serviceOrderItem.id=" + orderItem.getId());
            serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
            buildOrderMessageIfNeeded(orderItem, serviceOrder, response);
        } else {
            CreateMacroServiceInstanceResponse createMacroServiceInstanceResponse = response.getBody();
            if (createMacroServiceInstanceResponse != null && !orderItem.getState().equals(StateType.FAILED)) {
                orderItem.getService().setId(createMacroServiceInstanceResponse.getRequestReferences().getInstanceId());
                orderItem.setRequestId(createMacroServiceInstanceResponse.getRequestReferences().getRequestId());
            }

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                        || response.getBody().getRequestReferences() == null) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                LOGGER
                    .warn("order item {} failed , status {} , response {}", orderItem.getId(), response.getStatusCode(),
                        response.getBody());
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.INPROGRESS);
            }
        }
    }
    
    private void updateOrderItemToInProgress(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem) {
        if (serviceOrderItem.getAction() != ActionType.MODIFY) {
            serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem, StateType.INPROGRESS);
        } else {
            if (StateType.ACKNOWLEDGED == serviceOrderItem.getState()) {
                serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem,
                        StateType.INPROGRESS_MODIFY_REQUEST_DELETE_SEND);
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem,
                        StateType.INPROGRESS_MODIFY_REQUEST_CREATE_SEND);
            }
        }
    }

    private void buildOrderMessageIfNeeded(ServiceOrderItem serviceOrderItem, ServiceOrder serviceOrder,
            ResponseEntity<?> response) {
        if (response != null) {
            if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                serviceOrderService.addOrderMessage(serviceOrder, "502");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ResponseEntity<?> messageError = response;
                if (messageError.getBody().toString().toLowerCase().contains("serviceinstance already exists")) {
                    serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "105");
                } else {
                    serviceOrderService.addOrderItemMessageRequestSo(serviceOrder, serviceOrderItem,
                            messageError.getBody().toString());
                }
            }
        }
    }

    /**
     * Update E2EServiceOrderItem with SO response by using serviceOrderRepository with the serviceOrderId
     */
    private void updateE2EServiceOrderItem(ResponseEntity<CreateE2EServiceInstanceResponse> response,
            ServiceOrderItem orderItem, ServiceOrder serviceOrder) {

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            LOGGER.warn("response ko for serviceOrderItem.id=" + orderItem.getId());
            serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
        } else {
            CreateE2EServiceInstanceResponse createE2EServiceInstanceResponse = response.getBody();
            if (createE2EServiceInstanceResponse != null && !orderItem.getState().equals(StateType.FAILED)) {
                orderItem.getService().setId(createE2EServiceInstanceResponse.getService().getServiceId());
                orderItem.setRequestId(createE2EServiceInstanceResponse.getService().getOperationId());
            }

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || response.getBody().getService().getOperationId() == null
                    || response.getBody().getService().getServiceId() == null) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                LOGGER.warn("order item {} failed , status {} , response {}", orderItem.getId(),
                        response.getStatusCode(), response.getBody());
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.INPROGRESS);
            }
        }
    }

    /**
     * Update an executionTask in database when it's process with a success
     */
    private void updateSuccessTask(ExecutionTask executionTask) {
        executionTaskRepository.deleteById(executionTask.getInternalId());
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
            LOGGER.warn("task {} with orderitem id {} deleted cause orderitem id {} failed ", taskId.getInternalId(),
                    taskId.getOrderItemId(), executionTask.getOrderItemId());
        }
        for (ServiceOrderItem item : serviceOrder.getOrderItem()) {
            for (ExecutionTask taskToDelete : executionTasksToDelete) {
                if (taskToDelete.getOrderItemId().equals(item.getId())) {
                    serviceOrderService.updateOrderItemState(serviceOrder, item, StateType.FAILED);
                    LOGGER.warn("task {} with orderitem id {} failed cause orderitem id {} failed ",
                            taskToDelete.getInternalId(), taskToDelete.getOrderItemId(),
                            executionTask.getOrderItemId());

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

    private boolean shouldStopPolling(ExecutionTask executionTask) {
        long createTimeinMillis = executionTask.getCreateDate().getTime();
        long lastAttemptTimeInMillis = executionTask.getLastAttemptDate().getTime();
        long differenceInMillis = lastAttemptTimeInMillis - createTimeinMillis;
        float pollingDurationInMillis = pollingDurationInMins * 60000;
        LOGGER.debug("Task {} with orderitem id {}: Task create date: {} Task last attempt date: {}",
                executionTask.getInternalId(), executionTask.getOrderItemId(), createTimeinMillis,
                lastAttemptTimeInMillis);
        LOGGER.debug("Difference {} and Polling Duration {}", differenceInMillis, pollingDurationInMins);
        return (differenceInMillis > pollingDurationInMillis);
    }
}