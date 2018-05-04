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

import java.util.Date;
import java.util.Map;
import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CreateAAIServiceTypeManager {

    @Autowired
    private MultiClient serviceOrderConsumerService;

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAAIServiceTypeManager.class);

    public void createAAIServiceType(ServiceOrder serviceOrder, ServiceOrderInfo serviceOrderInfo) {

        Map servicesInAaiForCustomer = serviceOrderConsumerService
            .getServicesInAaiForCustomer(serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId());

        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            if (ActionType.ADD == serviceOrderItem.getAction()) {
                ServiceOrderItemInfo serviceOrderItemInfo =
                    serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId());
                String sdcServiceName = (String) serviceOrderItemInfo.getCatalogResponse().get("name");
                if (!serviceNameExistsInAAI(servicesInAaiForCustomer, sdcServiceName)) {
                    boolean serviceCreated = serviceOrderConsumerService.putServiceType(
                        serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId(), sdcServiceName);
                    if (!serviceCreated) {
                        serviceOrder.setState(StateType.REJECTED);
                        serviceOrder.setCompletionDateTime(new Date());
                        serviceOrderRepository.save(serviceOrder);
                        LOGGER.warn("serviceOrder {} rejected : cannot create service type {} for customer {}",
                            serviceOrder.getId(), sdcServiceName,
                            serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId());
                    }
                }
            }
        }

    }

    private boolean serviceNameExistsInAAI(Map servicesInAaiForCustomer, String sdcServiceName) {

        if (servicesInAaiForCustomer != null && servicesInAaiForCustomer.get("service-subscription") != null) {
            List<LinkedHashMap> servicesInAAI =
                (List<LinkedHashMap>) servicesInAaiForCustomer.get("service-subscription");
            for (LinkedHashMap service : servicesInAAI) {
                String serviceType = (String) service.get("service-type");
                if (sdcServiceName.equalsIgnoreCase(serviceType)) {
                    return true;
                }

            }
        }
        return false;

    }

}
