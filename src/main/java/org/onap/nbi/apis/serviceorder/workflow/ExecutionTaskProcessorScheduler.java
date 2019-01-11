package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("prod")
@Service
@EnableScheduling
public class ExecutionTaskProcessorScheduler {

    @Autowired
    ExecutionTaskRepository executionTaskRepository;

    @Autowired
    SOTaskProcessor soTaskProcessor;

    // Using fixedDelay to mitigate against Scheduler queue backlog with fixedRate
    @Scheduled(fixedDelayString = "${executionTask.schedule}", initialDelayString = "${executionTask.initial}")
    private void processExecutionPlan() throws InterruptedException {
        List<ExecutionTask> taskToExecute = executionTaskRepository.findByReliedTasksIsEmpty();
        for (ExecutionTask executionTask : taskToExecute) {
            soTaskProcessor.processOrderItem(executionTask);
        }
    }
}
