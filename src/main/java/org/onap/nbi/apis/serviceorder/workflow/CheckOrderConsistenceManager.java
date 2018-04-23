/**
 * Copyright (c) 2018 Orange
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;

@Service
public class CheckOrderConsistenceManager {


    @Autowired
    private MultiClient serviceOrderConsumerService;

    public ServiceOrderInfo checkServiceOrder(ServiceOrder serviceOrder) {
        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        manageCustomer(serviceOrder, serviceOrderInfo);
        int nbItemsCompleted = 0;
        boolean isServiceOrderRejected = false;
        boolean isAllItemsInAdd = true;

        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            ServiceOrderItemInfo serviceOrderItemInfo = new ServiceOrderItemInfo();
            serviceOrderItemInfo.setId(serviceOrderItem.getId());
            handleServiceFromCatalog(serviceOrderItem, serviceOrderItemInfo);
            switch (serviceOrderItem.getAction()) {
                case ADD:
                    if (existServiceInCatalog(serviceOrderItemInfo)
                            && StringUtils.isEmpty(serviceOrderItem.getService().getId())
                            && serviceOrderConsumerService.isTenantIdPresentInAAI()) {
                        serviceOrderInfo.addServiceOrderItemInfos(serviceOrderItem.getId(), serviceOrderItemInfo);
                    } else {
                        isServiceOrderRejected = true;
                        serviceOrderItem.setState(StateType.REJECTED);
                    }
                    break;
                case MODIFY:
                case DELETE:
                    isAllItemsInAdd = false;
                    if (isCustomerFromServiceOrderPresentInInventory(serviceOrderInfo)
                            && existServiceInInventory(serviceOrderItem, serviceOrderItemInfo,
                            serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId())) {
                        serviceOrderInfo.addServiceOrderItemInfos(serviceOrderItem.getId(), serviceOrderItemInfo);
                    } else {
                        isServiceOrderRejected = true;
                        serviceOrderItem.setState(StateType.REJECTED);
                    }
                    break;
                case NOCHANGE:
                    isAllItemsInAdd = false;
                    serviceOrderItem.setState(StateType.COMPLETED);
                    nbItemsCompleted++;
                    break;
            }
        }
        if (serviceOrder.getOrderItem().size() != nbItemsCompleted) {
            serviceOrderInfo.setAllItemsCompleted(false);
        } else {
            serviceOrderInfo.setAllItemsCompleted(true);
        }
        serviceOrderInfo.setAllItemsInAdd(isAllItemsInAdd);
        serviceOrderInfo.setIsServiceOrderRejected(isServiceOrderRejected);

        return serviceOrderInfo;

    }

    private void manageCustomer(ServiceOrder serviceOrder, ServiceOrderInfo serviceOrderInfo) {
        RelatedParty customerFromServiceOrder = getCustomerFromServiceOrder(serviceOrder);
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        if (customerFromServiceOrder != null) {
            subscriberInfo.setSubscriberName(customerFromServiceOrder.getName());
            subscriberInfo.setGlobalSubscriberId(customerFromServiceOrder.getId());
            serviceOrderInfo.setUseServiceOrderCustomer(true);
        } else {
            subscriberInfo.setSubscriberName("generic");
            subscriberInfo.setGlobalSubscriberId("generic");
            serviceOrderInfo.setUseServiceOrderCustomer(false);
        }
        serviceOrderInfo.setSubscriberInfo(subscriberInfo);

    }


    private RelatedParty getCustomerFromServiceOrder(ServiceOrder serviceOrder) {
        if (serviceOrder.getRelatedParty() != null) {
            for (RelatedParty relatedParty : serviceOrder.getRelatedParty()) {
                if ("ONAPcustomer".equalsIgnoreCase(relatedParty.getRole())) {
                    return relatedParty;
                }
            }
        }
        return null;
    }

    private boolean isCustomerFromServiceOrderPresentInInventory(ServiceOrderInfo serviceOrderInfo) {

        if (serviceOrderInfo.isUseServiceOrderCustomer()) {

            boolean customerPresentInAAI = serviceOrderConsumerService
                    .isCustomerPresentInAAI(serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId());
            return customerPresentInAAI;
        }
        return true;
    }

    private boolean existServiceInInventory(ServiceOrderItem serviceOrderItem,
                                            ServiceOrderItemInfo serviceOrderItemInfo, String globalSubscriberId) {
        if (!StringUtils.isEmpty(serviceOrderItem.getService().getId())) {
            String serviceName = (String) serviceOrderItemInfo.getCatalogResponse().get("name");
            boolean serviceExistInInventory = serviceOrderConsumerService.doesServiceExistInServiceInventory(
                    serviceOrderItem.getService().getId(), serviceName, globalSubscriberId);
            if (serviceExistInInventory) {
                return true;
            }
        }
        return false;
    }

    private boolean existServiceInCatalog(ServiceOrderItemInfo serviceOrderItemInfo) {
        return serviceOrderItemInfo.getCatalogResponse() != null;
    }

    private void handleServiceFromCatalog(ServiceOrderItem serviceOrderItem,
                                          ServiceOrderItemInfo serviceOrderItemInfo) {
        ResponseEntity<Object> response = serviceOrderConsumerService
                .getServiceCatalog(serviceOrderItem.getService().getServiceSpecification().getId());
        if (response != null && (response.getStatusCode().equals(HttpStatus.OK)
                || response.getStatusCode().equals(HttpStatus.PARTIAL_CONTENT))) {
            LinkedHashMap body = (LinkedHashMap) response.getBody();
            serviceOrderItemInfo.setCatalogResponse(body);
        }
    }


}
