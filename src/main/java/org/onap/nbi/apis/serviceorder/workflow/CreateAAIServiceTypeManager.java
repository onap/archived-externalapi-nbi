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

import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreateAAIServiceTypeManager {

    @Autowired
    private MultiClient serviceOrderConsumerService;

    @Autowired
    ServiceOrderService serviceOrderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAAIServiceTypeManager.class);

    public void createAAIServiceType(ServiceOrder serviceOrder, ServiceOrderInfo serviceOrderInfo) {
        Map servicesInAaiForCustomer = serviceOrderConsumerService.getServicesInAaiForCustomer(
                serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId(), serviceOrder);

        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            if (ActionType.ADD == serviceOrderItem.getAction()) {
                ServiceOrderItemInfo serviceOrderItemInfo =
                        serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId());
                String serviceTypeFromJson = serviceOrderItem.getService().getServicetype();
                String serviceType = serviceTypeFromJson != null ? serviceTypeFromJson : (String) serviceOrderItemInfo.getCatalogResponse().get("name");
                if (!serviceNameExistsInAAI(servicesInAaiForCustomer, serviceType)) {
                    boolean serviceCreated = serviceOrderConsumerService.putServiceType(
                            serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId(), serviceType, serviceOrder);
                    if (!serviceCreated) {
                        serviceOrderService.updateOrderState(serviceOrder, StateType.REJECTED);
                        LOGGER.warn("serviceOrder {} rejected : cannot create service type {} for customer {}",
                                serviceOrder.getId(), serviceType,
                                serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId());
                        serviceOrderService.addOrderMessage(serviceOrder, "501");

                    }
                }
            }
        }

    }

    private boolean serviceNameExistsInAAI(Map servicesInAaiForCustomer, String serviceType) {

        if (servicesInAaiForCustomer != null && servicesInAaiForCustomer.get("service-subscription") != null) {
            List<LinkedHashMap> servicesInAAI =
                    (List<LinkedHashMap>) servicesInAaiForCustomer.get("service-subscription");
            for (LinkedHashMap service : servicesInAAI) {
                String serviceTypeInAAI = (String) service.get("service-type");
                if (serviceType.equalsIgnoreCase(serviceTypeInAAI)) {
                    return true;
                }

            }
        }
        return false;

    }

}
