package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.model.OrderItemRelationship;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfoJson;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderInfoRepository;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
public class SOTaskManager {

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    @Autowired
    private ServiceOrderInfoRepository serviceOrderInfoRepository;

    @Autowired
    private SOTaskProcessor soTaskProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOTaskManager.class);

    /**
     * @param orderItems
     * @param serviceOrderInfoJson
     */
    private void registerOrderItemExecutionPlan(List<ServiceOrderItem> orderItems,
            ServiceOrderInfoJson serviceOrderInfoJson) {
        List<ExecutionTask> executionTasksSaved = new ArrayList<>();
        Map<String, Long> internalIdOrderItemsMap = new HashMap<>();
        if (orderItems != null) {
            // first we save create all the execution tasks with order item id in relied tasks
            for (ServiceOrderItem orderItem : orderItems) {
                ExecutionTask task = new ExecutionTask();
                task.setOrderItemId(orderItem.getId());
                task.setNbRetries(3);
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
                for (String key : internalIdOrderItemsMap.keySet()) {
                    String replace = executionTask.getReliedTasks().replace(key,
                            String.valueOf(internalIdOrderItemsMap.get(key)));
                    executionTask.setReliedTasks(replace);
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
        String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);
        ServiceOrderInfoJson serviceOrderInfoJson = new ServiceOrderInfoJson(serviceOrder.getId(), json);
        serviceOrderInfoRepository.save(serviceOrderInfoJson);
        registerOrderItemExecutionPlan(serviceOrder.getOrderItem(), serviceOrderInfoJson);
    }


    @Scheduled(fixedRate = 2000)
    private void processExecutionPlan() {
        List<ExecutionTask> taskToExecute = executionTaskRepository.findByReliedTasksIsEmpty();
        for (ExecutionTask executionTask : taskToExecute) {
            soTaskProcessor.processOrderItem(executionTask);
        }
    }
}
