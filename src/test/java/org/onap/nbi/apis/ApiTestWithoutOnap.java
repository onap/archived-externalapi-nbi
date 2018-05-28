/**
 *     Copyright (c) 2018 Orange
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.onap.nbi.apis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.assertions.ServiceOrderAssertions;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.workflow.SOTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiTestWithoutOnap {


    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    @Autowired
    ExecutionTaskRepository executionTaskRepository;

    @Autowired
    SOTaskProcessor SoTaskProcessor;

    @After
    public void tearsDownUpPort() throws Exception {
        executionTaskRepository.deleteAll();
        serviceOrderRepository.deleteAll();
    }


    public ExecutionTask getExecutionTask(String orderItemId) {
        for (ExecutionTask executionTask : executionTaskRepository.findAll()) {
            if (executionTask.getOrderItemId().equalsIgnoreCase(orderItemId)) {
                return executionTask;
            }

        }
        return null;
    }



    @Test
    public void testExecutionTaskWithoutOnap() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderAssertions.setUpBddForExecutionTaskSucess(serviceOrderRepository,
            executionTaskRepository, ActionType.ADD);

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);
    }






}
