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

package org.onap.nbi.test;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.assertions.ServiceOrderExecutionTaskAssertions;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.workflow.SOTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.github.tomakehurst.wiremock.admin.model.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ExecutionTaskTest {

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    @Autowired
    ExecutionTaskRepository executionTaskRepository;

    @Autowired
    SOTaskProcessor SoTaskProcessor;

    static Validator validator;

    @Before
    public void before() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Context.startWiremock();
    }

    @AfterClass
    public static void tearsDown() throws Exception {
        Context.stopServers();

    }

    @After
    public void tearsDownUpPort() throws Exception {
        executionTaskRepository.deleteAll();
        serviceOrderRepository.deleteAll();
        Context.wireMockServer.resetToDefaultMappings();

    }

    public ExecutionTask getExecutionTask(String orderItemId) {
        for (ExecutionTask executionTask : executionTaskRepository.findAll()) {
            if (executionTask.getOrderItemId().equalsIgnoreCase(orderItemId)) {
                return executionTask;
            }

        }
        return null;
    }

    private void changeWireMockResponse(String s, int statusCode, String bodyContent) {
        ListStubMappingsResult listStubMappingsResult = Context.wireMockServer.listAllStubMappings();
        ResponseDefinition responseDefinition = new ResponseDefinition(statusCode, bodyContent);
        List<StubMapping> mappings = listStubMappingsResult.getMappings();
        for (StubMapping mapping : mappings) {
            if (mapping.getRequest().getUrl().equals(s)) {
                mapping.setResponse(responseDefinition);
            }
        }
    }

    private ServiceOrder getServiceOrder(String serviceOrderId) {
        Optional<ServiceOrder> serviceOrderChecked = serviceOrderRepository.findById(serviceOrderId);
        return serviceOrderChecked.get();
    }

    @Test
    public void testExecutionTaskSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testE2EExecutionTaskSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForE2EExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testE2EExecutionTaskSuccessWithObject() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions.setUpBddForE2EExecutionTaskSucessWithObject(
                serviceOrderRepository, executionTaskRepository, ActionType.ADD);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testE2EExecutionTaskSuccessWithComplexObject() throws Exception {
        // A Service Order with complex object including arrays
        ExecutionTask executionTaskA =
                ServiceOrderExecutionTaskAssertions.setUpBddForE2EExecutionTaskSucessWithComplexObject(
                        serviceOrderRepository, executionTaskRepository, ActionType.ADD);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskDeleteSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.DELETE);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }
 @Test
    public void testE2EExecutionTaskActivationSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForE2EExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.MODIFY);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }


    @Test
    public void testE2EExecutionTaskDeleteSuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForE2EExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.DELETE);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }
    
 // Macro Flow Execution Task
 	@Test
 	public void testMacroExecutionTaskSuccess() throws Exception {

 		ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
 				.setUpBddForMacroExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);
 		ExecutionTask executionTaskB;

 		SoTaskProcessor.processOrderItem(executionTaskA);
 		ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
 		serviceOrderChecked = getServiceOrder("test");

 		assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
 		for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
 			assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

 		}
 	}
 	
 	// Macro Flow Execution Task for VNF with Service and VNF level Params
 	 @Test
 	    public void testMacroExecutionTaskSuccessForVNFWithServceAndVnfLevelParams() throws Exception {

 	        ExecutionTask executionTaskA =
 	            ServiceOrderExecutionTaskAssertions.setUpBddForMacroExecutionTaskSucessVnf(
 	                serviceOrderRepository, executionTaskRepository, ActionType.ADD);
 	        ExecutionTask executionTaskB;

 	        SoTaskProcessor.processOrderItem(executionTaskA);
 	        ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
 	        serviceOrderChecked = getServiceOrder("test");

 	        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
 	        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
 	            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

 	        }
 	    }
 	
 	// Macro Flow Execution Task with CNF with InstanceParams
 		@Test
 		public void testMacroExecutionTaskSuccessforCNFWithServiceAndVNFLevelParams() throws Exception {

 			ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
 					.setUpBddForMacroExecutionTaskSucessForCNF(serviceOrderRepository, executionTaskRepository, ActionType.ADD);
 			ExecutionTask executionTaskB;

 			SoTaskProcessor.processOrderItem(executionTaskA);
 			ServiceOrder serviceOrderChecked = getServiceOrder("test");
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
 			serviceOrderChecked = getServiceOrder("test");

 			assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
 			for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
 				assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

 			}
 		}
    
    @Test
    public void testExecutionTaskFailed() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        Context.removeWireMockMapping("/onap/so/infra/orchestrationRequests/v7/requestId");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }
        executionTaskA = getExecutionTask("A");
        Date createDate = executionTaskA.getCreateDate();
        assertThat(executionTaskA.getLastAttemptDate().getTime() > createDate.getTime()).isTrue();

        changeCreationDate(executionTaskA);
        SoTaskProcessor.processOrderItem(executionTaskA);

        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    private void changeCreationDate(ExecutionTask executionTaskA) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(executionTaskA.getCreateDate());
        cal.add(Calendar.SECOND, -30);
        executionTaskA.setCreateDate(cal.getTime());
    }

    @Test
    public void testE2EExecutionTaskFailed() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForE2EExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        Context.removeWireMockMapping("/onap/so/infra/e2eServiceInstances/v3/serviceId/operations/operationId");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }
        executionTaskA = getExecutionTask("A");
        assertThat(executionTaskA.getLastAttemptDate().getTime() > executionTaskA.getCreateDate().getTime()).isTrue();
        changeCreationDate(executionTaskA);
        SoTaskProcessor.processOrderItem(executionTaskA);

        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskFailedNoSoResponse() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        Context.removeWireMockMapping("/onap/so/infra/serviceInstantiation/v7/serviceInstances");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskFailedNoSoAndStatusResponse() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        Context.removeWireMockMapping("/onap/so/infra/serviceInstantiation/v7/serviceInstances");
        Context.removeWireMockMapping("/onap/so/infra/orchestrationRequests/v7/requestId");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testE2EExecutionTaskFailedNoSoAndStatusResponse() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForE2EExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        Context.removeWireMockMapping("/onap/so/infra/e2eServiceInstances/v3");
        Context.removeWireMockMapping("/onap/so/infra/e2eServiceInstances/v3/serviceId/operations/operationId");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskFailedBadRequestSo() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        changeWireMockResponse("/onap/so/infra/serviceInstantiation/v7/serviceInstances", 400,
                "\"serviceException\": {\n" + "        \"messageId\": \"SVC0002\",\n"
                        + "        \"text\": \"Error parsing request.  org.openecomp.mso.apihandler.common.ValidationException: serviceInstance already existsd\"\n"
                        + "    }");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                assertThat(serviceOrderItem.getOrderItemMessage().size()).isEqualTo(1);
                assertThat(serviceOrderItem.getOrderItemMessage().get(0).getCode()).isEqualTo("105");
                assertThat(serviceOrderItem.getOrderItemMessage().get(0).getField()).isEqualTo("service.name");
            }
        }

    }

    @Test
    public void testExecutionTaskModifySuccess() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.MODIFY);
        ExecutionTask executionTaskB;

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                // assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS);
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }
        SoTaskProcessor.processOrderItem(executionTaskA);
        serviceOrderChecked = getServiceOrder("test");
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
        SoTaskProcessor.processOrderItem(executionTaskB);

        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.COMPLETED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.COMPLETED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskModifyFailed() throws Exception {

        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.MODIFY);
        ExecutionTask executionTaskB;
        Context.removeWireMockMapping("/onap/so/infra/orchestrationRequests/v7/requestId");

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.INPROGRESS);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            if (serviceOrderItem.getId().equals("A")) {
                // assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS);
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.INPROGRESS_MODIFY_REQUEST_DELETE_SEND);
            } else {
                assertThat(serviceOrderItem.getState()).isEqualTo(StateType.ACKNOWLEDGED);
            }
        }
        executionTaskA = getExecutionTask("A");
        assertThat(executionTaskA.getLastAttemptDate().getTime() > executionTaskA.getCreateDate().getTime()).isTrue();
        changeCreationDate(executionTaskA);
        SoTaskProcessor.processOrderItem(executionTaskA);

        serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);

        }

        assertThat(executionTaskRepository.count()).isEqualTo(0);

    }

    @Test
    public void testExecutionTaskWithoutOnap() throws Exception {
        Context.stopWiremock();
        ExecutionTask executionTaskA = ServiceOrderExecutionTaskAssertions
                .setUpBddForExecutionTaskSucess(serviceOrderRepository, executionTaskRepository, ActionType.ADD);

        SoTaskProcessor.processOrderItem(executionTaskA);
        ServiceOrder serviceOrderChecked = getServiceOrder("test");
        assertThat(serviceOrderChecked.getState()).isEqualTo(StateType.FAILED);
        for (ServiceOrderItem serviceOrderItem : serviceOrderChecked.getOrderItem()) {
            assertThat(serviceOrderItem.getState()).isEqualTo(StateType.FAILED);
        }
        assertThat(serviceOrderChecked.getOrderMessage().size()).isGreaterThan(0);
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getCode()).isEqualTo("502");
        assertThat(serviceOrderChecked.getOrderMessage().get(0).getMessageInformation())
                .isEqualTo("Problem with SO API");

        assertThat(executionTaskRepository.count()).isEqualTo(0);
        Context.startWiremock();

    }

}
