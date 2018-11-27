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

import java.util.*;
import java.util.Map.Entry;
import org.onap.nbi.apis.serviceorder.model.OrderItemRelationship;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class SOTaskManager {

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    @Autowired
    private SOTaskProcessor soTaskProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOTaskManager.class);

    /**
     * @param orderItems
     * @param serviceOrderInfoJson
     */
    private void registerOrderItemExecutionPlan(List<ServiceOrderItem> orderItems,
        String serviceOrderInfoJson) {
        List<ExecutionTask> executionTasksSaved = new ArrayList<>();
        Map<String, Long> internalIdOrderItemsMap = new HashMap<>();
        if (orderItems != null) {
            // first we save create all the execution tasks with order item id in relied tasks
            for (ServiceOrderItem orderItem : orderItems) {
                ExecutionTask task = new ExecutionTask();
                task.setOrderItemId(orderItem.getId());
                task.setCreateDate(new Date());
                StringBuilder sb = new StringBuilder();
                for (OrderItemRelationship orderItemRelationship : orderItem.getOrderItemRelationship()) {
                    sb.append(orderItemRelationship.getId()).append(" ");
                }
                task.setReliedTasks(sb.toString());
                task.setServiceOrderInfoJson(serviceOrderInfoJson);
                ExecutionTask savedTask = executionTaskRepository.save(task);
                executionTasksSaved.add(savedTask);

                internalIdOrderItemsMap.put(savedTask.getOrderItemId(), savedTask.getInternalId());
            }
            // then we replace all orderitem ids in reliedtasks field with internalid of the tasks
            for (ExecutionTask executionTask : executionTasksSaved) {
                List<String> reliedOrderItemsIds = new ArrayList<String>(Arrays.asList(executionTask.getReliedTasks().split(" ")));
                List<String> reliedTasksInternalIds = new ArrayList<String>();
                for (Entry<String, Long> entry : internalIdOrderItemsMap.entrySet()){
                   if(reliedOrderItemsIds.contains(entry.getKey())) {
                       reliedTasksInternalIds.add(entry.getValue().toString());
                    }
                }
            String reliedTasksString = String.join(" ", reliedTasksInternalIds);
            executionTask.setReliedTasks(reliedTasksString);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("saving task with id {} , orderItemId {} , reliedtasks {}", executionTask.getInternalId(), executionTask.getOrderItemId(), executionTask.getReliedTasks());
                }
                executionTaskRepository.save(executionTask);
            }
        }
    }

    /**
     *
     * @param serviceOrder
     * @param serviceOrderInfo
     */
    public void registerServiceOrder(ServiceOrder serviceOrder, ServiceOrderInfo serviceOrderInfo) {
        String serviceOrderInfoJson = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);
        registerOrderItemExecutionPlan(serviceOrder.getOrderItem(), serviceOrderInfoJson);
    }

    // Using fixedDelay to mitigate against Scheduler queue backlog with fixedRate 
    @Scheduled(fixedDelay = 2000)
    private void processExecutionPlan() throws InterruptedException {
        List<ExecutionTask> taskToExecute = executionTaskRepository.findByReliedTasksIsEmpty();
        for (ExecutionTask executionTask : taskToExecute) {
            soTaskProcessor.processOrderItem(executionTask);
        }
    }
}
