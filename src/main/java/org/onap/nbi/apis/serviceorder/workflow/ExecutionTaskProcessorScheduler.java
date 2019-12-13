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

import java.util.List;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("default")
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
