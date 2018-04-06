/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.assertions.ServiceCatalogAssertions;
import org.onap.nbi.apis.assertions.ServiceInventoryAssertions;
import org.onap.nbi.apis.assertions.ServiceOrderAssertions;
import org.onap.nbi.apis.servicecatalog.ServiceSpecificationResource;
import org.onap.nbi.apis.serviceinventory.ServiceInventoryResource;
import org.onap.nbi.apis.serviceorder.ServiceOrderResource;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.onap.nbi.apis.serviceorder.model.Service;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderInfoRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.workflow.SOTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiTest {

    @LocalServerPort
    int randomServerPort;

    String realServerPort;

    static public WireMockServer wireMockServer = new WireMockServer(8091);

    @Autowired
    ServiceSpecificationResource serviceSpecificationResource;

    @Autowired
    ServiceInventoryResource serviceInventoryResource;

    @Autowired
    ServiceOrderResource serviceOrderResource;

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    @Autowired
    ServiceOrderInfoRepository serviceOrderInfoRepository;

    @Autowired
    ExecutionTaskRepository executionTaskRepository;

    @Autowired
    SOTaskProcessor SoTaskProcessor;

    static Validator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        wireMockServer.start();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterClass
    public static void tearsDown() throws Exception {
        wireMockServer.stop();

    }

    @After
    public void tearsDownUpPort() throws Exception {
        executionTaskRepository.deleteAll();
        serviceOrderInfoRepository.deleteAll();
        serviceOrderRepository.deleteAll();
        wireMockServer.resetToDefaultMappings();

    }


    public ExecutionTask getExecutionTask(String orderItemId) {
        for (ExecutionTask executionTask : executionTaskRepository.findAll()) {
            if (executionTask.getOrderItemId().equalsIgnoreCase(orderItemId)) {
                return executionTask;
            }

        }
        return null;
    }

    // serviceCatalog

    @Test
    @Ignore
    public void testServiceResourceGetCatalog() throws Exception {

        ResponseEntity<Object> resource =
                serviceSpecificationResource.getServiceSpecification("1e3feeb0-8e36-46c6-862c-236d9c626439", null);
        ServiceCatalogAssertions.assertGetServiceCatalog(resource);

    }

    @Test
    public void testServiceCatalogGetResourceWithoutTosca() throws Exception {

        ResponseEntity<Object> resource = serviceSpecificationResource
                .getServiceSpecification("1e3feeb0-8e36-46c6-862c-236d9c626439_withoutTosca", null);
        ServiceCatalogAssertions.asserGetServiceCatalogWithoutTosca(resource);

    }

    @Test
    public void testServiceCatalogFind() throws Exception {

        ResponseEntity<Object> resource = serviceSpecificationResource.findServiceSpecification(null);
        ServiceCatalogAssertions.assertFindServiceCatalog(resource);

    }


    @Test
    public void testServiceCatalogFindWithFilter() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "name");
        ResponseEntity<Object> resource = serviceSpecificationResource.findServiceSpecification(params);
        ServiceCatalogAssertions.assertFindServiceCatalogWIthFilter(resource);

    }

    // serviceInventory

    @Test
    public void testServiceResourceGetInventory() throws Exception {

        String serviceName = "vFW";
        String serviceId = "e4688e5f-61a0-4f8b-ae02-a2fbde623bcb";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.name", serviceName);
        params.add("relatedParty.id", "6490");
        ResponseEntity<Object> resource = serviceInventoryResource.getServiceInventory(serviceId, params);
        ServiceInventoryAssertions.assertServiceInventoryGet(resource);

    }


    @Test
    public void testServiceResourceGetInventoryWithServiceSpecId() throws Exception {

        String serviceId = "e4688e5f-61a0-4f8b-ae02-a2fbde623bcb";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.id", "1e3feeb0-8e36-46c6-862c-236d9c626439");
        params.add("relatedParty.id", "6490");
        ResponseEntity<Object> resource = serviceInventoryResource.getServiceInventory(serviceId, params);
        ServiceInventoryAssertions.assertServiceInventoryGet(resource);

    }


    @Test
    public void testServiceInventoryFind() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String serviceName = "vFW";
        params.add("serviceSpecification.name", serviceName);
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFind(resource);

    }


    @Test
    public void testServiceInventoryFindWithServiceSpecId() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.id", "1e3feeb0-8e36-46c6-862c-236d9c626439");
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFind(resource);

    }


    @Test
    public void testServiceInventoryFindWithoutParameter() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFindWithoutParameter(resource);

    }

    @Test
    public void testCreateServiceOrderResource() throws Exception {

        ResponseEntity<Object> serviceOrder = serviceOrderResource
                .createServiceOrder(ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD), null, null);
        assertThat(serviceOrder.getStatusCodeValue()).isEqualTo(201);
        ServiceOrder body = (ServiceOrder) serviceOrder.getBody();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getState()).isEqualTo(StateType.ACKNOWLEDGED);


    }

    @Test
    public void testCheckServiceOrder() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.ACKNOWLEDGED);

    }

    @Test
    public void testCheckServiceOrderWithGenericCustomer() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        testServiceOrder.setRelatedParty(new ArrayList<>());
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.ACKNOWLEDGED);

    }

    @Test
    public void testCheckServiceOrderWithUnKnonwCustomer() throws Exception {

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

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.ACKNOWLEDGED);

    }

    @Test
    public void testCheckServiceOrderDelete() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.DELETE);
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.ACKNOWLEDGED);

    }

    @Test
    public void testCheckServiceOrderDeleteRejected() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.DELETE);
        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.getService().setId("no_response");

        }
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.REJECTED);

    }

    @Test
    public void testCheckServiceOrderNoChange() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.DELETE);
        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setAction(ActionType.NOCHANGE);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);
        }

    }



    @Test
    public void testCheckServiceOrderNoChangeAndDelete() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.DELETE);
        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                serviceOrderItem.setAction(ActionType.NOCHANGE);
            }
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.ACKNOWLEDGED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A"))
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);
        }
    }



    @Test
    public void testCheckServiceOrderRejected() throws Exception {


        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrderRejected();
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        serviceOrderResource.scheduleCheckServiceOrders();

        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.REJECTED);

    }

    @Test
    public void validateServiceOrderBeanWithAnnotation() {
        ServiceOrder serviceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        Set violations = validator.validate(serviceOrder);
        assertThat(violations).isEmpty();

        ServiceOrderItem item = new ServiceOrderItem();
        item.setAction(ActionType.DELETE);
        item.setService(new Service());
        serviceOrder.addOrderItemItem(item);

        violations = validator.validate(serviceOrder);
        assertThat(violations).isNotEmpty();
    }



    @Test
    public void testFindAndGetServiceOrder() throws Exception {

        ServiceOrder testServiceOrder = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }
        testServiceOrder.setExternalId("extid1");
        testServiceOrder.setOrderDate(new Date());
        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setDescription("toto");
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);


        ServiceOrder testServiceOrder2 = ServiceOrderAssertions.createTestServiceOrder(ActionType.ADD);
        testServiceOrder2.setState(StateType.ACKNOWLEDGED);
        testServiceOrder2.setDescription("toto");
        testServiceOrder2.setId("test2");
        testServiceOrder2.setExternalId("extid2");
        testServiceOrder2.setOrderDate(new Date());

        serviceOrderRepository.save(testServiceOrder2);


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "id");

        ResponseEntity<Object> serviceOrdersResponse = serviceOrderResource.findServiceOrder(params);
        ArrayList serviceOrders = (ArrayList) serviceOrdersResponse.getBody();
        assertThat(serviceOrders.size()).isEqualTo(2);

        params.add("externalId", "extid1");
        params.add("state", "acknowledged");

        serviceOrdersResponse = serviceOrderResource.findServiceOrder(params);
        serviceOrders = (ArrayList) serviceOrdersResponse.getBody();
        assertThat(serviceOrders.size()).isEqualTo(1);

        MultiValueMap<String, String> paramsDate = new LinkedMultiValueMap<>();
        paramsDate.add("orderDate.gt", "2017-01-01 00:00:00.000");
        paramsDate.add("orderDate.lt", "2030-01-01 00:00:00.000");
        paramsDate.add("offset", "0");
        paramsDate.add("limit", "2");

        serviceOrdersResponse = serviceOrderResource.findServiceOrder(paramsDate);
        serviceOrders = (ArrayList) serviceOrdersResponse.getBody();
        assertThat(serviceOrders.size()).isEqualTo(2);


        ResponseEntity<Object> serviceOrderResponse =
                serviceOrderResource.getServiceOrder("test2", new LinkedMultiValueMap<>());
        ServiceOrder serviceOrder = (ServiceOrder) serviceOrderResponse.getBody();
        assertThat(serviceOrder).isNotNull();
    }



    @Test
    public void testExecutionTaskSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderAssertions.setUpBddForExecutionTaskSucess(serviceOrderRepository,
                serviceOrderInfoRepository, executionTaskRepository, ActionType.ADD);
        ExecutionTask executionTaskB;


        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }

        executionTaskB = getExecutionTask("B");
        assertThat(executionTaskB.getReliedTasks()).isNullOrEmpty();
        executionTaskA = getExecutionTask("A");
        assertThat(executionTaskA).isNull();

        SoTaskProcessor.processOrderItem(executionTaskB);
        serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        executionTaskB = getExecutionTask("B");
        assertThat(executionTaskB).isNull();


    }


    @Test
    public void testExecutionTaskDeleteSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderAssertions.setUpBddForExecutionTaskSucess(serviceOrderRepository,
                serviceOrderInfoRepository, executionTaskRepository, ActionType.DELETE);
        ExecutionTask executionTaskB;


        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }

        executionTaskB = getExecutionTask("B");
        assertThat(executionTaskB.getReliedTasks()).isNullOrEmpty();
        executionTaskA = getExecutionTask("A");
        assertThat(executionTaskA).isNull();

        SoTaskProcessor.processOrderItem(executionTaskB);
        serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        executionTaskB = getExecutionTask("B");
        assertThat(executionTaskB).isNull();


    }


    @Test
    public void testExecutionTaskFailed() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderAssertions.setUpBddForExecutionTaskSucess(serviceOrderRepository,
                serviceOrderInfoRepository, executionTaskRepository, ActionType.ADD);

        ListStubMappingsResult listStubMappingsResult = wireMockServer.listAllStubMappings();
        StubMapping mappingToDelete = null;
        List<StubMapping> mappings = listStubMappingsResult.getMappings();
        for (StubMapping mapping : mappings) {
            if (mapping.getRequest().getUrl().equals("/ecomp/mso/infra/orchestrationRequests/v4/requestId")) {
                mappingToDelete = mapping;
            }
        }
        wireMockServer.removeStubMapping(mappingToDelete);


        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }
        executionTaskA = getExecutionTask("A");
        assertThat(executionTaskA.getNbRetries()).isEqualTo(2);
        SoTaskProcessor.processOrderItem(executionTaskA);
        executionTaskA = getExecutionTask("A");
        SoTaskProcessor.processOrderItem(executionTaskA);
        executionTaskA = getExecutionTask("A");
        SoTaskProcessor.processOrderItem(executionTaskA);

        serviceOrderChecked = serviceOrderRepository.findOne("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);

        }

        ExecutionTask executionTaskB = executionTaskRepository.findOne(Long.parseLong("2"));
        assertThat(executionTaskB).isNull();


    }

}
