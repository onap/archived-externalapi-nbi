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

package org.onap.nbi.apis.assertions;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.OrderItemRelationship;
import org.onap.nbi.apis.serviceorder.model.OrderRelationship;
import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.onap.nbi.apis.serviceorder.model.RelationshipType;
import org.onap.nbi.apis.serviceorder.model.ResourceSpecification;
import org.onap.nbi.apis.serviceorder.model.Service;
import org.onap.nbi.apis.serviceorder.model.ServiceCharacteristic;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.ServiceSpecificationRef;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.Value;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;
import org.onap.nbi.apis.serviceorder.repositories.ExecutionTaskRepository;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.onap.nbi.apis.serviceorder.utils.JsonEntityConverter;

public class ServiceOrderExecutionTaskAssertions {

    public static ServiceOrder createTestServiceOrderRejected() {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setExternalId("LudONAP001");
        serviceOrder.setPriority("1");
        serviceOrder.setDescription("Ludo first ONAP Order");
        serviceOrder.setCategory("Consumer");
        serviceOrder.setRequestedStartDate(new Date());
        serviceOrder.setRequestedCompletionDate(new Date());

        RelatedParty party = new RelatedParty();
        party.setId("6490");
        party.setRole("ONAPcustomer");
        party.setReferredType("individual");
        party.setName("Jean Pontus");
        List<RelatedParty> relatedPartyList = new ArrayList<>();
        relatedPartyList.add(party);
        serviceOrder.setRelatedParty(relatedPartyList);

        List<ServiceOrderItem> items = new ArrayList<>();

        ServiceOrderItem itemA = new ServiceOrderItem();
        itemA.id("A");
        itemA.action(ActionType.ADD);
        Service serviceA = new Service();
        serviceA.setServiceState("active");
        ServiceSpecificationRef serviceSpecificationRefA = new ServiceSpecificationRef();
        serviceSpecificationRefA.setId("333");
        serviceA.setServiceSpecification(serviceSpecificationRefA);
        itemA.setService(serviceA);
        items.add(itemA);

        ServiceOrderItem itemB = new ServiceOrderItem();
        itemB.id("B");
        itemB.action(ActionType.ADD);
        Service serviceB = new Service();
        serviceB.setServiceState("active");
        ServiceSpecificationRef serviceSpecificationRefB = new ServiceSpecificationRef();
        serviceSpecificationRefB.setId("1e3feeb0-8e36-46c6-862c-236d9c626439");
        serviceB.setServiceSpecification(serviceSpecificationRefB);
        itemB.setService(serviceB);
        List<OrderItemRelationship> orderItemRelationships = new ArrayList<>();
        OrderItemRelationship orderItemRelationship = new OrderItemRelationship();
        orderItemRelationship.setId("A");
        orderItemRelationships.add(orderItemRelationship);
        itemB.setOrderItemRelationship(orderItemRelationships);
        items.add(itemB);
        serviceOrder.setOrderItem(items);
        return serviceOrder;

    }

    public static ServiceOrder createTestServiceOrder(ActionType actionType) {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setExternalId("LudONAP001");
        serviceOrder.setPriority("1");
        serviceOrder.setDescription("Ludo first ONAP Order");
        serviceOrder.setCategory("Consumer");
        serviceOrder.setRequestedStartDate(new Date());
        serviceOrder.setRequestedCompletionDate(new Date());
        serviceOrder.setBaseType("toto");
        serviceOrder.setCompletionDateTime(new Date());
        serviceOrder.setExpectedCompletionDate(new Date());
        serviceOrder.setSchemaLocation("/tutu");

        OrderRelationship orderRelationship = new OrderRelationship();
        orderRelationship.setId("test");
        orderRelationship.setHref("test");
        orderRelationship.setReferredType("test");
        orderRelationship.setType("type");
        List<OrderRelationship> relationships = new ArrayList<>();
        serviceOrder.setOrderRelationship(relationships);

        RelatedParty party = new RelatedParty();
        party.setId("6490");
        party.setRole("ONAPcustomer");
        party.setReferredType("individual");
        party.setName("Jean Pontus");
        List<RelatedParty> relatedPartyList = new ArrayList<>();
        relatedPartyList.add(party);
        serviceOrder.setRelatedParty(relatedPartyList);

        List<ServiceOrderItem> items = new ArrayList<>();

        ServiceOrderItem itemA = new ServiceOrderItem();
        itemA.id("A");
        itemA.action(actionType);
        Service serviceA = new Service();
        if (actionType != ActionType.ADD) {
            serviceA.setId("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
        }
        serviceA.setServiceState("active");
        ServiceSpecificationRef serviceSpecificationRefA = new ServiceSpecificationRef();
        serviceSpecificationRefA.setId("1e3feeb0-8e36-46c6-862c-236d9c626439");
        serviceA.setServiceSpecification(serviceSpecificationRefA);
        itemA.setService(serviceA);
        items.add(itemA);

        ServiceOrderItem itemB = new ServiceOrderItem();
        itemB.id("B");
        itemB.action(actionType);
        Service serviceB = new Service();
        if (actionType != ActionType.ADD) {
            serviceB.setId("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
        }
        serviceB.setServiceState("active");
        ServiceSpecificationRef serviceSpecificationRefB = new ServiceSpecificationRef();
        serviceSpecificationRefB.setId("1e3feeb0-8e36-46c6-862c-236d9c626439");
        serviceB.setServiceSpecification(serviceSpecificationRefB);
        itemB.setService(serviceB);
        List<OrderItemRelationship> orderItemRelationships = new ArrayList<>();
        OrderItemRelationship orderItemRelationship = new OrderItemRelationship();
        orderItemRelationship.setId("A");
        orderItemRelationship.setType(RelationshipType.RELIESON);
        orderItemRelationships.add(orderItemRelationship);
        itemB.setOrderItemRelationship(orderItemRelationships);
        items.add(itemB);
        serviceOrder.setOrderItem(items);
        return serviceOrder;

    }
    
    public static ServiceOrder createTestServiceOrderForMacroVnf(ActionType actionType) {
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setExternalId("LudONAP001");
		serviceOrder.setPriority("1");
		serviceOrder.setDescription("Ludo first ONAP Order");
		serviceOrder.setCategory("Consumer");
		serviceOrder.setRequestedStartDate(new Date());
		serviceOrder.setRequestedCompletionDate(new Date());
		serviceOrder.setBaseType("toto");
		serviceOrder.setCompletionDateTime(new Date());
		serviceOrder.setExpectedCompletionDate(new Date());
		serviceOrder.setSchemaLocation("/tutu");

		OrderRelationship orderRelationship = new OrderRelationship();
		orderRelationship.setId("test");
		orderRelationship.setHref("test");
		orderRelationship.setReferredType("test");
		orderRelationship.setType("type");
		List<OrderRelationship> relationships = new ArrayList<>();
		serviceOrder.setOrderRelationship(relationships);

		RelatedParty party = new RelatedParty();
		party.setId("6490");
		party.setRole("ONAPcustomer");
		party.setReferredType("individual");
		party.setName("Jean Pontus");
		List<RelatedParty> relatedPartyList = new ArrayList<>();
		relatedPartyList.add(party);
		serviceOrder.setRelatedParty(relatedPartyList);

		List<ServiceOrderItem> items = new ArrayList<>();

		ServiceOrderItem itemA = new ServiceOrderItem();
		itemA.id("A");
		itemA.action(actionType);
		Service serviceA = new Service();
	
		serviceA.setServiceState("active");
		ServiceSpecificationRef serviceSpecificationRefA = new ServiceSpecificationRef();
		serviceSpecificationRefA.setId("3bed38ff-a4fd-4463-9784-caf738d46dbc");
		serviceA.setServiceSpecification(serviceSpecificationRefA);
		itemA.setService(serviceA);
		items.add(itemA);

		ServiceOrderItem itemB = new ServiceOrderItem();
		itemB.id("B");
		itemB.action(actionType);
		Service serviceB = new Service();
		if (actionType != ActionType.ADD) {
			serviceB.setId("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
		}
		serviceB.setServiceState("active");
		ServiceSpecificationRef serviceSpecificationRefB = new ServiceSpecificationRef();
		serviceSpecificationRefB.setId("3bed38ff-a4fd-4463-9784-caf738d46dbc");
		serviceB.setServiceSpecification(serviceSpecificationRefB);
		itemB.setService(serviceB);
		List<OrderItemRelationship> orderItemRelationships = new ArrayList<>();
		OrderItemRelationship orderItemRelationship = new OrderItemRelationship();
		orderItemRelationship.setId("A");
		orderItemRelationship.setType(RelationshipType.RELIESON);
		orderItemRelationships.add(orderItemRelationship);
		itemB.setOrderItemRelationship(orderItemRelationships);
		items.add(itemB);
		serviceOrder.setOrderItem(items);
		return serviceOrder;
	}


    public static ServiceOrder createTestServiceOrderForMacro(ActionType actionType) {
	ServiceOrder serviceOrder = new ServiceOrder();
	serviceOrder.setExternalId("LudONAP001");
	serviceOrder.setPriority("1");
	serviceOrder.setDescription("Ludo first ONAP Order");
	serviceOrder.setCategory("Consumer");
	serviceOrder.setRequestedStartDate(new Date());
	serviceOrder.setRequestedCompletionDate(new Date());
	serviceOrder.setBaseType("toto");
	serviceOrder.setCompletionDateTime(new Date());
	serviceOrder.setExpectedCompletionDate(new Date());
	serviceOrder.setSchemaLocation("/tutu");

	OrderRelationship orderRelationship = new OrderRelationship();
	orderRelationship.setId("test");
	orderRelationship.setHref("test");
	orderRelationship.setReferredType("test");
	orderRelationship.setType("type");
	List<OrderRelationship> relationships = new ArrayList<>();
	serviceOrder.setOrderRelationship(relationships);

	RelatedParty party = new RelatedParty();
	party.setId("6490");
	party.setRole("ONAPcustomer");
	party.setReferredType("individual");
	party.setName("Jean Pontus");
	List<RelatedParty> relatedPartyList = new ArrayList<>();
	relatedPartyList.add(party);
	serviceOrder.setRelatedParty(relatedPartyList);

	List<ServiceOrderItem> items = new ArrayList<>();

	ServiceOrderItem itemA = new ServiceOrderItem();
	itemA.id("A");
	itemA.action(actionType);
	Service serviceA = new Service();
	if (actionType != ActionType.ADD) {
		serviceA.setId("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
	}
	serviceA.setServiceState("active");
	ServiceSpecificationRef serviceSpecificationRefA = new ServiceSpecificationRef();
	serviceSpecificationRefA.setId("82c9fbb4-656c-4973-8c7f-172b22b5fa8f");
	serviceA.setServiceSpecification(serviceSpecificationRefA);
	itemA.setService(serviceA);
	items.add(itemA);

	ServiceOrderItem itemB = new ServiceOrderItem();
	itemB.id("B");
	itemB.action(actionType);
	Service serviceB = new Service();
	if (actionType != ActionType.ADD) {
		serviceB.setId("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
	}
	serviceB.setServiceState("active");
	ServiceSpecificationRef serviceSpecificationRefB = new ServiceSpecificationRef();
	serviceSpecificationRefB.setId("82c9fbb4-656c-4973-8c7f-172b22b5fa8f");
	serviceB.setServiceSpecification(serviceSpecificationRefB);
	itemB.setService(serviceB);
	List<OrderItemRelationship> orderItemRelationships = new ArrayList<>();
	OrderItemRelationship orderItemRelationship = new OrderItemRelationship();
	orderItemRelationship.setId("A");
	orderItemRelationship.setType(RelationshipType.RELIESON);
	orderItemRelationships.add(orderItemRelationship);
	itemB.setOrderItemRelationship(orderItemRelationships);
	items.add(itemB);
	serviceOrder.setOrderItem(items);
	return serviceOrder;

	}
    
    public static ExecutionTask setUpBddForExecutionTaskSucess(ServiceOrderRepository serviceOrderRepository,
            ExecutionTaskRepository executionTaskRepository, ActionType actionType) {
        ServiceOrder testServiceOrder = createTestServiceOrder(actionType);

        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        LinkedHashMap<String, String> sdcResponse = new LinkedHashMap<>();
        sdcResponse.put("invariantUUID", "uuid");
        sdcResponse.put("name", "vFW");
        sdcResponse.put("version", "v1");
        sdcResponse.put("category", "NonE2E");

        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        serviceOrderInfo.setServiceOrderId("test");
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId("6490");
        subscriberInfo.setSubscriberName("edgar");
        serviceOrderInfo.setSubscriberInfo(subscriberInfo);

        ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
        serviceOrderItemInfoA.setId("A");
        serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

        ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
        serviceOrderItemInfoB.setId("B");
        serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
        serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
        serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

        String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

        ExecutionTask executionTaskA = new ExecutionTask();
        executionTaskA.setCreateDate(new Date());
        executionTaskA.setOrderItemId("A");
        executionTaskA.setServiceOrderInfoJson(json);
        executionTaskA = executionTaskRepository.save(executionTaskA);
        ExecutionTask executionTaskB = new ExecutionTask();
        executionTaskB.setCreateDate(new Date());
        executionTaskB.setOrderItemId("B");
        executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
        executionTaskB.setServiceOrderInfoJson(json);
        executionTaskRepository.save(executionTaskB);
        return executionTaskA;
    }

    public static ExecutionTask setUpBddForE2EExecutionTaskSucess(ServiceOrderRepository serviceOrderRepository,
            ExecutionTaskRepository executionTaskRepository, ActionType actionType) {
        ServiceOrder testServiceOrder = createTestServiceOrder(actionType);

        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
            List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
            ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
            serviceCharacteristic1.setName("access-site-id");
            Value value1 = new Value();
            value1.setServiceCharacteristicValue("1234765");
            serviceCharacteristic1.setValue(value1);
            serviceCharacteristics.add(serviceCharacteristic1);
            ServiceCharacteristic serviceCharacteristic2 = new ServiceCharacteristic();
            serviceCharacteristic2.setName("provider-site-id");
            Value value2 = new Value();
            value2.setServiceCharacteristicValue("654321");
            serviceCharacteristic2.setValue(value2);
            serviceCharacteristics.add(serviceCharacteristic2);
            serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
        sdcResponse.put("invariantUUID", "uuid");
        sdcResponse.put("name", "vFW");
        sdcResponse.put("version", "v1");
        sdcResponse.put("category", "E2E Service");

        List<ResourceSpecification> resourceSpecs = new ArrayList<>();
        ResourceSpecification resourceSpecificationA = new ResourceSpecification();
        resourceSpecificationA.setId("2e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecificationA.setInstanceName("vFW-vSINK");
        resourceSpecificationA.setName("vFW-SINK");
        resourceSpecificationA.setType("ONAPresource");
        resourceSpecificationA.setVersion("2.0");
        resourceSpecificationA.setResourceInvariantUUID("6e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecs.add(resourceSpecificationA);

        sdcResponse.put("resourceSpecification", resourceSpecs);

        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        serviceOrderInfo.setServiceOrderId("test");
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId("6490");
        subscriberInfo.setSubscriberName("edgar");
        serviceOrderInfo.setSubscriberInfo(subscriberInfo);

        ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
        serviceOrderItemInfoA.setId("A");
        serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

        ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
        serviceOrderItemInfoB.setId("B");
        serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
        serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
        serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

        String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

        ExecutionTask executionTaskA = new ExecutionTask();
        executionTaskA.setCreateDate(new Date());
        executionTaskA.setOrderItemId("A");
        executionTaskA.setServiceOrderInfoJson(json);
        executionTaskA = executionTaskRepository.save(executionTaskA);
        ExecutionTask executionTaskB = new ExecutionTask();
        executionTaskB.setCreateDate(new Date());
        executionTaskB.setOrderItemId("B");
        executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
        executionTaskB.setServiceOrderInfoJson(json);
        executionTaskRepository.save(executionTaskB);
        return executionTaskA;
    }

    public static ExecutionTask setUpBddForE2EExecutionTaskSucessWithObject(
            ServiceOrderRepository serviceOrderRepository, ExecutionTaskRepository executionTaskRepository,
            ActionType actionType) {
        ServiceOrder testServiceOrder = createTestServiceOrder(actionType);

        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
            List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
            ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
            serviceCharacteristic1.setName("EPL_ServiceCharacteristics");
            serviceCharacteristic1.setValueType("object");
            Value value1 = new Value();
            value1.setServiceCharacteristicValue("{ \"key1\":\"value1\", \"key2\":\"value2\"}");
            serviceCharacteristic1.setValue(value1);
            serviceCharacteristics.add(serviceCharacteristic1);
            serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
        sdcResponse.put("invariantUUID", "uuid");
        sdcResponse.put("name", "vFW");
        sdcResponse.put("version", "v1");
        sdcResponse.put("category", "E2E Service");

        List<ResourceSpecification> resourceSpecs = new ArrayList<>();
        ResourceSpecification resourceSpecificationA = new ResourceSpecification();
        resourceSpecificationA.setId("2e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecificationA.setInstanceName("vFW-vSINK");
        resourceSpecificationA.setName("vFW-SINK");
        resourceSpecificationA.setType("ONAPresource");
        resourceSpecificationA.setVersion("2.0");
        resourceSpecificationA.setResourceInvariantUUID("6e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecs.add(resourceSpecificationA);

        sdcResponse.put("resourceSpecification", resourceSpecs);

        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        serviceOrderInfo.setServiceOrderId("test");
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId("6490");
        subscriberInfo.setSubscriberName("edgar");
        serviceOrderInfo.setSubscriberInfo(subscriberInfo);

        ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
        serviceOrderItemInfoA.setId("A");
        serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

        ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
        serviceOrderItemInfoB.setId("B");
        serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
        serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
        serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

        String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

        ExecutionTask executionTaskA = new ExecutionTask();
        executionTaskA.setCreateDate(new Date());
        executionTaskA.setOrderItemId("A");
        executionTaskA.setServiceOrderInfoJson(json);
        executionTaskA = executionTaskRepository.save(executionTaskA);
        ExecutionTask executionTaskB = new ExecutionTask();
        executionTaskB.setCreateDate(new Date());
        executionTaskB.setOrderItemId("B");
        executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
        executionTaskB.setServiceOrderInfoJson(json);
        executionTaskRepository.save(executionTaskB);
        return executionTaskA;
    }

    public static ExecutionTask setUpBddForE2EExecutionTaskSucessWithComplexObject(
            ServiceOrderRepository serviceOrderRepository, ExecutionTaskRepository executionTaskRepository,
            ActionType actionType) {
        ServiceOrder testServiceOrder = createTestServiceOrder(actionType);

        for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
            List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
            ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
            serviceCharacteristic1.setName("EPL_ServiceCharacteristics");
            serviceCharacteristic1.setValueType("object");
            Value value1 = new Value();
            // Three parameters , one is an array of child objects
            value1.setServiceCharacteristicValue(
                    "{\"key1\":\"value1\", \"key2\":\"value2\", \"key3\":[ {\"lat1\":\"value1\",\"lon1\":\"value1\"}, { \"lat2\":\"value2\", \"lon2\":\"value2\"}]}");
            serviceCharacteristic1.setValue(value1);
            serviceCharacteristics.add(serviceCharacteristic1);
            serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
        }

        testServiceOrder.setState(StateType.ACKNOWLEDGED);
        testServiceOrder.setId("test");
        serviceOrderRepository.save(testServiceOrder);

        LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
        sdcResponse.put("invariantUUID", "uuid");
        sdcResponse.put("name", "vFW");
        sdcResponse.put("version", "v1");
        sdcResponse.put("category", "E2E Service");

        List<ResourceSpecification> resourceSpecs = new ArrayList<>();
        ResourceSpecification resourceSpecificationA = new ResourceSpecification();
        resourceSpecificationA.setId("2e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecificationA.setInstanceName("vFW-vSINK");
        resourceSpecificationA.setName("vFW-SINK");
        resourceSpecificationA.setType("ONAPresource");
        resourceSpecificationA.setVersion("2.0");
        resourceSpecificationA.setResourceInvariantUUID("6e3feeb0-8e36-46c6-862c-236d9c626438");
        resourceSpecs.add(resourceSpecificationA);

        sdcResponse.put("resourceSpecification", resourceSpecs);

        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        serviceOrderInfo.setServiceOrderId("test");
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId("6490");
        subscriberInfo.setSubscriberName("edgar");
        serviceOrderInfo.setSubscriberInfo(subscriberInfo);

        ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
        serviceOrderItemInfoA.setId("A");
        serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

        ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
        serviceOrderItemInfoB.setId("B");
        serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
        serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
        serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

        String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

        ExecutionTask executionTaskA = new ExecutionTask();
        executionTaskA.setCreateDate(new Date());
        executionTaskA.setOrderItemId("A");
        executionTaskA.setServiceOrderInfoJson(json);
        executionTaskA = executionTaskRepository.save(executionTaskA);
        ExecutionTask executionTaskB = new ExecutionTask();
        executionTaskB.setCreateDate(new Date());
        executionTaskB.setOrderItemId("B");
        executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
        executionTaskB.setServiceOrderInfoJson(json);
        executionTaskRepository.save(executionTaskB);
        return executionTaskA;
    }
    
    public static ExecutionTask setUpBddForMacroExecutionTaskSucessVnf(ServiceOrderRepository serviceOrderRepository,
			ExecutionTaskRepository executionTaskRepository, ActionType actionType) {

		ServiceOrder testServiceOrder = createTestServiceOrderForMacroVnf(actionType);

		for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
			serviceOrderItem.setState(StateType.ACKNOWLEDGED);
			List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
			ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
			serviceCharacteristic1.setName("serviceProperty1");
			Value value1 = new Value();
			value1.setServiceCharacteristicValue("1234765");
			serviceCharacteristic1.setValue(value1);
			serviceCharacteristics.add(serviceCharacteristic1);
			ServiceCharacteristic serviceCharacteristic2 = new ServiceCharacteristic();
			serviceCharacteristic2.setName("dummy_vsp0_vfLevelProperty2");
			Value value2 = new Value();
			value2.setServiceCharacteristicValue("654321");
			serviceCharacteristic2.setValue(value2);
			serviceCharacteristics.add(serviceCharacteristic2);
			serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
		}

		testServiceOrder.setState(StateType.ACKNOWLEDGED);
		testServiceOrder.setId("test");
		serviceOrderRepository.save(testServiceOrder);

		LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
		sdcResponse.put("invariantUUID", "b96687b0-5353-4e4d-bbd5-a49796fc3f11");
		sdcResponse.put("name", "dummy_service");
		sdcResponse.put("version", "2.0");
		sdcResponse.put("category", "Network Service");
		sdcResponse.put("instantiationType", "Macro");

		List<ResourceSpecification> resourceSpecs = new ArrayList<>();
		ResourceSpecification resourceSpecificationA = new ResourceSpecification();
		resourceSpecificationA.setId("725860b4-1a98-4da6-9451-6c04a581eb44");
		resourceSpecificationA.setInstanceName("dummy_vsp");
		resourceSpecificationA.setName("dummy_vsp");
		resourceSpecificationA.setType("VF");
		resourceSpecificationA.setVersion("1.0");
		resourceSpecificationA.setResourceInvariantUUID("0fa0d33d-b268-42ce-b393-7d3c6fde6ca9");
		resourceSpecs.add(resourceSpecificationA);

		sdcResponse.put("resourceSpecification", resourceSpecs);

		ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
		serviceOrderInfo.setServiceOrderId("test");
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setGlobalSubscriberId("6490");
		subscriberInfo.setSubscriberName("edgar");
		serviceOrderInfo.setSubscriberInfo(subscriberInfo);

		ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
		serviceOrderItemInfoA.setId("A");
		serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

		ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
		serviceOrderItemInfoB.setId("B");
		serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
		serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
		serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

		

		String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

		ExecutionTask executionTaskA = new ExecutionTask();
		executionTaskA.setCreateDate(new Date());
		executionTaskA.setOrderItemId("A");
		executionTaskA.setServiceOrderInfoJson(json);
		executionTaskA = executionTaskRepository.save(executionTaskA);
		ExecutionTask executionTaskB = new ExecutionTask();
		executionTaskB.setCreateDate(new Date());
		executionTaskB.setOrderItemId("B");
		executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
		executionTaskB.setServiceOrderInfoJson(json);
		executionTaskRepository.save(executionTaskB);
		return executionTaskA;
	    }
    
    public static ServiceOrder createTestServiceOrderForMacroCNF(ActionType actionType) {
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setExternalId("LudONAP001");
		serviceOrder.setPriority("1");
		serviceOrder.setDescription("Ludo first ONAP Order");
		serviceOrder.setCategory("Consumer");
		serviceOrder.setRequestedStartDate(new Date());
		serviceOrder.setRequestedCompletionDate(new Date());
		serviceOrder.setBaseType("toto");
		serviceOrder.setCompletionDateTime(new Date());
		serviceOrder.setExpectedCompletionDate(new Date());
		serviceOrder.setSchemaLocation("/tutu");

		OrderRelationship orderRelationship = new OrderRelationship();
		orderRelationship.setId("test");
		orderRelationship.setHref("test");
		orderRelationship.setReferredType("test");
		orderRelationship.setType("type");
		List<OrderRelationship> relationships = new ArrayList<>();
		serviceOrder.setOrderRelationship(relationships);

		RelatedParty party = new RelatedParty();
		party.setId("6490");
		party.setRole("ONAPcustomer");
		party.setReferredType("individual");
		party.setName("Jean Pontus");
		List<RelatedParty> relatedPartyList = new ArrayList<>();
		relatedPartyList.add(party);
		serviceOrder.setRelatedParty(relatedPartyList);

		List<ServiceOrderItem> items = new ArrayList<>();

		ServiceOrderItem itemA = new ServiceOrderItem();
		itemA.id("A");
		itemA.action(actionType);
		Service serviceA = new Service();

		serviceA.setId("edf094cc-281f-4be9-a284-e047ded86066");
		serviceA.setServicetype("vfwk8s");

		serviceA.setServiceState("active");
		ServiceSpecificationRef serviceSpecificationRefA = new ServiceSpecificationRef();
		serviceSpecificationRefA.setId("edf094cc-281f-4be9-a284-e047ded86066");
		serviceA.setServiceSpecification(serviceSpecificationRefA);
		itemA.setService(serviceA);
		items.add(itemA);

		ServiceOrderItem itemB = new ServiceOrderItem();
		itemB.id("B");
		itemB.action(actionType);
		Service serviceB = new Service();
		//serviceB.setId("edf094cc-281f-4be9-a284-e047ded86066");

		serviceB.setServiceState("active");
		ServiceSpecificationRef serviceSpecificationRefB = new ServiceSpecificationRef();
		serviceSpecificationRefB.setId("edf094cc-281f-4be9-a284-e047ded86066");
		serviceB.setServiceSpecification(serviceSpecificationRefB);
		itemB.setService(serviceB);
		List<OrderItemRelationship> orderItemRelationships = new ArrayList<>();
		OrderItemRelationship orderItemRelationship = new OrderItemRelationship();
		orderItemRelationship.setId("A");
		orderItemRelationship.setType(RelationshipType.RELIESON);
		orderItemRelationships.add(orderItemRelationship);
		itemB.setOrderItemRelationship(orderItemRelationships);
		items.add(itemB);
		serviceOrder.setOrderItem(items);
		System.out.println(serviceOrder);
		return serviceOrder;
    }
    
  //setup for CNF MacroFLow
    public static ExecutionTask setUpBddForMacroExecutionTaskSucessForCNF(ServiceOrderRepository serviceOrderRepository,
			ExecutionTaskRepository executionTaskRepository, ActionType actionType) {

		ServiceOrder testServiceOrder = createTestServiceOrderForMacroCNF(actionType);

		for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
			serviceOrderItem.setState(StateType.ACKNOWLEDGED);
			List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
			ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
			serviceCharacteristic1.setName("vfw_cnf_13080_dummy_vf_2");
			Value value1 = new Value();
			value1.setServiceCharacteristicValue("ggg");
			serviceCharacteristic1.setValue(value1);
			serviceCharacteristics.add(serviceCharacteristic1);
			ServiceCharacteristic serviceCharacteristic2 = new ServiceCharacteristic();
			serviceCharacteristic2.setName("vfw_cnf_13080_dummy_vf_1");
			Value value2 = new Value();
			value2.setServiceCharacteristicValue("hhhh");
			serviceCharacteristic2.setValue(value2);
			serviceCharacteristics.add(serviceCharacteristic2);
			ServiceCharacteristic serviceCharacteristic3 = new ServiceCharacteristic();
			serviceCharacteristic3.setName("dummy_ser_2");
			Value value3 = new Value();
			value3.setServiceCharacteristicValue("serv2");
			serviceCharacteristic3.setValue(value3);
			serviceCharacteristics.add(serviceCharacteristic3);
			ServiceCharacteristic serviceCharacteristic4 = new ServiceCharacteristic();
			serviceCharacteristic4.setName("dummy_ser_1");
			Value value4 = new Value();
			value4.setServiceCharacteristicValue("serv1");
			serviceCharacteristic4.setValue(value4);
			serviceCharacteristics.add(serviceCharacteristic4);
			
			serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
		}

		testServiceOrder.setState(StateType.ACKNOWLEDGED);
		testServiceOrder.setId("test");
		serviceOrderRepository.save(testServiceOrder);

		LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
		sdcResponse.put("invariantUUID", "uuid");
		sdcResponse.put("name", "vfw_cnf_1308");
		sdcResponse.put("version", "1.0");
		sdcResponse.put("category", "Network Service");
		sdcResponse.put("instantiationType", "Macro");

		List<ResourceSpecification> resourceSpecs = new ArrayList<>();
		ResourceSpecification resourceSpecificationA = new ResourceSpecification();
		resourceSpecificationA.setId("679effb6-35e7-425e-9272-4b4e6b2b8382");
		resourceSpecificationA.setInstanceName("vfw_cnf_1308 0");
		resourceSpecificationA.setName("vfw_cnf_1308 0");
		resourceSpecificationA.setType("VF");
		resourceSpecificationA.setVersion("1.0");
		resourceSpecificationA.setResourceInvariantUUID("2c9870d3-21cd-4140-afa0-3ba770bef206");
		resourceSpecs.add(resourceSpecificationA);

		sdcResponse.put("resourceSpecification", resourceSpecs);

		ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
		serviceOrderInfo.setServiceOrderId("test");
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setGlobalSubscriberId("6490");
		subscriberInfo.setSubscriberName("edgar");
		serviceOrderInfo.setSubscriberInfo(subscriberInfo);

		ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
		serviceOrderItemInfoA.setId("A");
		serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

		ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
		serviceOrderItemInfoB.setId("B");
		serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
		serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
		serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

		String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

		ExecutionTask executionTaskA = new ExecutionTask();
		executionTaskA.setCreateDate(new Date());
		executionTaskA.setOrderItemId("A");
		executionTaskA.setServiceOrderInfoJson(json);
		executionTaskA = executionTaskRepository.save(executionTaskA);
		ExecutionTask executionTaskB = new ExecutionTask();
		executionTaskB.setCreateDate(new Date());
		executionTaskB.setOrderItemId("B");
		executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
		executionTaskB.setServiceOrderInfoJson(json);
		executionTaskRepository.save(executionTaskB);
		return executionTaskA;
    }
    
    public static ExecutionTask setUpBddForMacroExecutionTaskSucess(ServiceOrderRepository serviceOrderRepository,
		ExecutionTaskRepository executionTaskRepository, ActionType actionType) {

	ServiceOrder testServiceOrder = createTestServiceOrderForMacro(actionType);

	for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
		serviceOrderItem.setState(StateType.ACKNOWLEDGED);
		List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
		ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
		serviceCharacteristic1.setName("access-site-id");
		Value value1 = new Value();
		value1.setServiceCharacteristicValue("1234765");
		serviceCharacteristic1.setValue(value1);
		serviceCharacteristics.add(serviceCharacteristic1);
		ServiceCharacteristic serviceCharacteristic2 = new ServiceCharacteristic();
		serviceCharacteristic2.setName("provider-site-id");
		Value value2 = new Value();
		value2.setServiceCharacteristicValue("654321");
		serviceCharacteristic2.setValue(value2);
		serviceCharacteristics.add(serviceCharacteristic2);
		serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
	}

	testServiceOrder.setState(StateType.ACKNOWLEDGED);
	testServiceOrder.setId("test");
	serviceOrderRepository.save(testServiceOrder);

	LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
	sdcResponse.put("invariantUUID", "uuid");
	sdcResponse.put("name", "VLB_Service");
	sdcResponse.put("version", "1.0");
	sdcResponse.put("category", "Network Service");
	sdcResponse.put("instantiationType", "Macro");

	List<ResourceSpecification> resourceSpecs = new ArrayList<>();
	ResourceSpecification resourceSpecificationA = new ResourceSpecification();
	resourceSpecificationA.setId("35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc");
	resourceSpecificationA.setInstanceName("vLB_VSP");
	resourceSpecificationA.setName("vLB_VSP");
	resourceSpecificationA.setType("VF");
	resourceSpecificationA.setVersion("1.0");
	resourceSpecificationA.setResourceInvariantUUID("368371e6-1d2f-4f4a-b992-a9053c7c2f1e");
	resourceSpecs.add(resourceSpecificationA);

	sdcResponse.put("resourceSpecification", resourceSpecs);

	ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
	serviceOrderInfo.setServiceOrderId("test");
	SubscriberInfo subscriberInfo = new SubscriberInfo();
	subscriberInfo.setGlobalSubscriberId("6490");
	subscriberInfo.setSubscriberName("edgar");
	serviceOrderInfo.setSubscriberInfo(subscriberInfo);

	ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
	serviceOrderItemInfoA.setId("A");
	serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

	ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
	serviceOrderItemInfoB.setId("B");
	serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
	serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
	serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

	

	String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

	ExecutionTask executionTaskA = new ExecutionTask();
	executionTaskA.setCreateDate(new Date());
	executionTaskA.setOrderItemId("A");
	executionTaskA.setServiceOrderInfoJson(json);
	executionTaskA = executionTaskRepository.save(executionTaskA);
	ExecutionTask executionTaskB = new ExecutionTask();
	executionTaskB.setCreateDate(new Date());
	executionTaskB.setOrderItemId("B");
	executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
	executionTaskB.setServiceOrderInfoJson(json);
	executionTaskRepository.save(executionTaskB);
	return executionTaskA;
    }

    public static ExecutionTask setUpBddForMacroExecutionTaskSucessWithObject(
	ServiceOrderRepository serviceOrderRepository, ExecutionTaskRepository executionTaskRepository,
	ActionType actionType) {
	ServiceOrder testServiceOrder = createTestServiceOrderForMacro(actionType);

	for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
		serviceOrderItem.setState(StateType.ACKNOWLEDGED);
		List<ServiceCharacteristic> serviceCharacteristics = new ArrayList();
		ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
		serviceCharacteristic1.setName("ServiceCharacteristics");
		serviceCharacteristic1.setValueType("object");
		Value value1 = new Value();
		value1.setServiceCharacteristicValue("{ \"key1\":\"value1\", \"key2\":\"value2\"}");
		serviceCharacteristic1.setValue(value1);
		serviceCharacteristics.add(serviceCharacteristic1);
		serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
	}

	testServiceOrder.setState(StateType.ACKNOWLEDGED);
	testServiceOrder.setId("test");
	serviceOrderRepository.save(testServiceOrder);

	LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
	sdcResponse.put("invariantUUID", "uuid");
	sdcResponse.put("name", "VLB_Service");
	sdcResponse.put("version", "1");
	sdcResponse.put("category", "Network Service");
	sdcResponse.put("instantiationType", "Macro");

	List<ResourceSpecification> resourceSpecs = new ArrayList<>();
	ResourceSpecification resourceSpecificationA = new ResourceSpecification();
	resourceSpecificationA.setId("35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc");
	resourceSpecificationA.setInstanceName("vLB_VSP");
	resourceSpecificationA.setName("vFW-SINK");
	resourceSpecificationA.setType("ONAPresource");
	resourceSpecificationA.setVersion("2.0");
	resourceSpecificationA.setResourceInvariantUUID("368371e6-1d2f-4f4a-b992-a9053c7c2f1e");
	resourceSpecs.add(resourceSpecificationA);

	sdcResponse.put("resourceSpecification", resourceSpecs);

	ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
	serviceOrderInfo.setServiceOrderId("test");
	SubscriberInfo subscriberInfo = new SubscriberInfo();
	subscriberInfo.setGlobalSubscriberId("6490");
	subscriberInfo.setSubscriberName("edgar");
	serviceOrderInfo.setSubscriberInfo(subscriberInfo);

	ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
	serviceOrderItemInfoA.setId("A");
	serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

	ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
	serviceOrderItemInfoB.setId("B");
	serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
	serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
	serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

	String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

	ExecutionTask executionTaskA = new ExecutionTask();
	executionTaskA.setCreateDate(new Date());
	executionTaskA.setOrderItemId("A");
	executionTaskA.setServiceOrderInfoJson(json);
	executionTaskA = executionTaskRepository.save(executionTaskA);
	ExecutionTask executionTaskB = new ExecutionTask();
	executionTaskB.setCreateDate(new Date());
	executionTaskB.setOrderItemId("B");
	executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
	executionTaskB.setServiceOrderInfoJson(json);
	executionTaskRepository.save(executionTaskB);
	return executionTaskA;
    }

    public static ExecutionTask setUpBddForMacroExecutionTaskSucessWithComplexObject(
		ServiceOrderRepository serviceOrderRepository, ExecutionTaskRepository executionTaskRepository,
		ActionType actionType) {
	ServiceOrder testServiceOrder = createTestServiceOrderForMacro(actionType);

	for (ServiceOrderItem serviceOrderItem : testServiceOrder.getOrderItem()) {
		serviceOrderItem.setState(StateType.ACKNOWLEDGED);
		List<ServiceCharacteristic> serviceCharacteristics = new ArrayList<>();
		ServiceCharacteristic serviceCharacteristic1 = new ServiceCharacteristic();
		serviceCharacteristic1.setName("ServiceCharacteristics");
		serviceCharacteristic1.setValueType("object");
		Value value1 = new Value();
		// Three parameters , one is an array of child objects
		value1.setServiceCharacteristicValue(
				"{\"key1\":\"value1\", \"key2\":\"value2\", \"key3\":[ {\"lat1\":\"value1\",\"lon1\":\"value1\"}, { \"lat2\":\"value2\", \"lon2\":\"value2\"}]}");
		serviceCharacteristic1.setValue(value1);
		serviceCharacteristics.add(serviceCharacteristic1);
		serviceOrderItem.getService().setServiceCharacteristic(serviceCharacteristics);
	}

	testServiceOrder.setState(StateType.ACKNOWLEDGED);
	testServiceOrder.setId("test");
	serviceOrderRepository.save(testServiceOrder);

	LinkedHashMap<String, Object> sdcResponse = new LinkedHashMap<>();
	sdcResponse.put("invariantUUID", "uuid");
	sdcResponse.put("name", "VLB_Service");
	sdcResponse.put("version", "1");
	sdcResponse.put("category", "Network Services");
	sdcResponse.put("instantiationType", "Macro");

	List<ResourceSpecification> resourceSpecs = new ArrayList<>();
	ResourceSpecification resourceSpecificationA = new ResourceSpecification();
	resourceSpecificationA.setId("35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc");
	resourceSpecificationA.setInstanceName("vLB_VSP");
	resourceSpecificationA.setName("vLB_VSP");
	resourceSpecificationA.setType("ONAPresource");
	resourceSpecificationA.setVersion("2.0");
	resourceSpecificationA.setResourceInvariantUUID("368371e6-1d2f-4f4a-b992-a9053c7c2f1e");
	resourceSpecs.add(resourceSpecificationA);

	sdcResponse.put("resourceSpecification", resourceSpecs);

	ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
	serviceOrderInfo.setServiceOrderId("test");
	SubscriberInfo subscriberInfo = new SubscriberInfo();
	subscriberInfo.setGlobalSubscriberId("6490");
	subscriberInfo.setSubscriberName("edgar");
	serviceOrderInfo.setSubscriberInfo(subscriberInfo);

	ServiceOrderItemInfo serviceOrderItemInfoA = new ServiceOrderItemInfo();
	serviceOrderItemInfoA.setId("A");
	serviceOrderItemInfoA.setCatalogResponse(sdcResponse);

	ServiceOrderItemInfo serviceOrderItemInfoB = new ServiceOrderItemInfo();
	serviceOrderItemInfoB.setId("B");
	serviceOrderItemInfoB.setCatalogResponse(sdcResponse);
	serviceOrderInfo.addServiceOrderItemInfos("A", serviceOrderItemInfoA);
	serviceOrderInfo.addServiceOrderItemInfos("B", serviceOrderItemInfoB);

	String json = JsonEntityConverter.convertServiceOrderInfoToJson(serviceOrderInfo);

	ExecutionTask executionTaskA = new ExecutionTask();
	executionTaskA.setCreateDate(new Date());
	executionTaskA.setOrderItemId("A");
	executionTaskA.setServiceOrderInfoJson(json);
	executionTaskA = executionTaskRepository.save(executionTaskA);
	ExecutionTask executionTaskB = new ExecutionTask();
	executionTaskB.setCreateDate(new Date());
	executionTaskB.setOrderItemId("B");
	executionTaskB.setReliedTasks(String.valueOf(executionTaskA.getInternalId()));
	executionTaskB.setServiceOrderInfoJson(json);
	executionTaskRepository.save(executionTaskB);
	return executionTaskA;
   }
}