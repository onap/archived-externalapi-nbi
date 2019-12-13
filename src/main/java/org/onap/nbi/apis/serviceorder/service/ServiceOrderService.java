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

package org.onap.nbi.apis.serviceorder.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.onap.nbi.apis.serviceorder.model.OrderMessage;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.SeverityMessage;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOrderService {

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    private static final String SERVICE_ID = "service.id";

    public Optional<ServiceOrder> findServiceOrderById(String serviceOrderId) {
        return serviceOrderRepository.findById(serviceOrderId);
    }

    public List<ServiceOrder> findServiceOrdersByState(StateType state) {
        return serviceOrderRepository.findByState(state);
    }

    public ServiceOrder updateOrderState(ServiceOrder serviceOrder, StateType state) {
        if (StateType.COMPLETED.equals(state) || StateType.REJECTED.equals(state)) {
            serviceOrder.setCompletionDateTime(new Date());
        }
        serviceOrder.setState(state);
        return serviceOrderRepository.save(serviceOrder);
    }

    public void updateOrderItemState(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem, StateType state) {
        serviceOrderItem.setState(state);
        serviceOrderRepository.save(serviceOrder);
    }

    public ServiceOrder createServiceOrder(ServiceOrder serviceOrder) {
        serviceOrder.setState(StateType.ACKNOWLEDGED);
        serviceOrder.setOrderDate(new Date());
        serviceOrder.setId(null);
        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }
        serviceOrder = serviceOrderRepository.save(serviceOrder);
        serviceOrder.setHref("serviceOrder/" + serviceOrder.getId());
        return serviceOrderRepository.save(serviceOrder);
    }

    public void deleteServiceOrder(String serviceOrderId) {
        serviceOrderRepository.deleteById(serviceOrderId);
    }

    public long countServiceOrder() {
        return serviceOrderRepository.count();
    }

    public void addOrderMessage(ServiceOrder serviceOrder, String code) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setCode(code);
        orderMessage.setSeverity(SeverityMessage.ERROR);
        orderMessage.setCorrectionRequired(true);

        if ("104".equalsIgnoreCase(code)) {
            orderMessage.setField("relatedParty.id");
            orderMessage.setMessageInformation("Referred customer did not exist in ONAP");
            serviceOrder.addOrderMessageItem(orderMessage);
        }
        if ("500".equalsIgnoreCase(code)) {
            orderMessage.setMessageInformation("Problem with SDC API");
            serviceOrder.addOrderMessageItem(orderMessage);
        }
        if ("501".equalsIgnoreCase(code)) {
            orderMessage.setMessageInformation("Problem with AAI API");
            serviceOrder.addOrderMessageItem(orderMessage);
        }
        if ("502".equalsIgnoreCase(code)) {
            orderMessage.setMessageInformation("Problem with SO API");
            serviceOrder.addOrderMessageItem(orderMessage);
        }
        if ("503".equalsIgnoreCase(code)) {
            orderMessage.setMessageInformation("tenantId not found in AAI");
            serviceOrder.addOrderMessageItem(orderMessage);
        }
        serviceOrderRepository.save(serviceOrder);
    }

    public void addOrderItemMessage(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem, String code) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setCode(code);
        orderMessage.setSeverity(SeverityMessage.ERROR);
        orderMessage.setCorrectionRequired(true);

        if ("101".equalsIgnoreCase(code)) {
            orderMessage.setField(SERVICE_ID);
            orderMessage.setMessageInformation("Missing Information - orderItem.service.id must be provided");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        if ("102".equalsIgnoreCase(code)) {
            orderMessage.setField("serviceSpecification.id");
            orderMessage
                    .setMessageInformation("Incorrect serviceSpecification.id provided – not found in Catalog (SDC");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        if ("103".equalsIgnoreCase(code)) {
            orderMessage.setField(SERVICE_ID);
            orderMessage.setMessageInformation(
                    "Inconsistence information provided. service.id must not be provided for add action");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        if ("105".equalsIgnoreCase(code)) {
            orderMessage.setField("service.name");
            orderMessage.setMessageInformation("ServiceName already exist in AAI");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        if ("106".equalsIgnoreCase(code)) {
            orderMessage.setField(SERVICE_ID);
            orderMessage.setMessageInformation("Incorrect service.id provided – not found in Inventory (AAI)");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        if ("504".equalsIgnoreCase(code)) {
            orderMessage.setMessageInformation("Service Orchestrator Service Instantiation timed out");
            serviceOrderItem.addOrderItemMessageItem(orderMessage);
        }
        serviceOrderRepository.save(serviceOrder);
    }

    public void addOrderItemMessageRequestSo(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem,
            String message) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setCode("105");
        orderMessage.setSeverity(SeverityMessage.ERROR);
        orderMessage.setCorrectionRequired(true);
        orderMessage.setMessageInformation(message);
        serviceOrderItem.addOrderItemMessageItem(orderMessage);
        serviceOrderRepository.save(serviceOrder);
    }

}
