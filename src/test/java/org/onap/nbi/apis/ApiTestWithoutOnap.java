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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.assertions.ServiceOrderAssertions;
import org.onap.nbi.apis.servicecatalog.ServiceSpecificationResource;
import org.onap.nbi.apis.serviceinventory.ServiceInventoryResource;
import org.onap.nbi.apis.serviceorder.ServiceOrderResource;
import org.onap.nbi.apis.serviceorder.model.*;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.workflow.SOTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiTestWithoutOnap {


    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    @Autowired
    ExecutionTaskRepository executionTaskRepository;

    @Autowired
    SOTaskProcessor SoTaskProcessor;

    @Autowired
    ServiceSpecificationResource serviceSpecificationResource;

    @Autowired
    ServiceInventoryResource serviceInventoryResource;

    @Autowired
    ServiceOrderResource serviceOrderResource;

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
        assertThat(serviceOrderChecked.getOrderMessage().size()).isGreaterThan(0);
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getCode()).isEqualTo("502");
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getMessageInformation()).isEqualTo("Problem with SO API");

        assertThat(executionTaskRepository.count()).isEqualTo(0);
    }


    @Test
    public void testCheckServiceOrderWithSDCNotResponding() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        List<RelatedParty> customers = new ArrayList<>();
        RelatedParty customer = new RelatedParty();
        customer.setId("new");
        customer.setRole("ONAPcustomer");
        customer.setName("romain");
        customers.add(customer);
        testServiceOrder.setRelatedParty(customers);
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.checkServiceOrder(testServiceOrder);

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.REJECTED);

        assertThat(serviceOrderChecked.getOrderMessage().size()).isGreaterThan(0);
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getCode()).isEqualTo("500");
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getMessageInformation()).isEqualTo("Problem with SDC API");
    }



    @Test
    public void testServiceCatalogGetResource() throws Exception {

        ResponseEntity<Object> resource = serviceSpecificationResource
            .getServiceSpecification("1e3feeb0-8e36-46c6-862c-236d9c626439", null);
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);


    }



    @Test
    public void testServiceResourceGetInventory() throws Exception {

        String serviceName = "vFW";
        String serviceId = "e4688e5f-61a0-4f8b-ae02-a2fbde623bcb";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.name", serviceName);
        params.add("relatedParty.id", "6490");
        ResponseEntity<Object> resource = serviceInventoryResource.getServiceInventory(serviceId, params);
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    }




}
