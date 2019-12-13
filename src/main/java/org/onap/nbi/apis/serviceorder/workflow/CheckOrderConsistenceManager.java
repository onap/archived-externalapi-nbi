/**
 * Copyright (c) 2018 Orange <p> Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at <p>
 * http://www.apache.org/licenses/LICENSE-2.0 <p> Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.onap.nbi.apis.serviceorder.workflow;

import java.util.Map;
import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.onap.nbi.apis.serviceorder.utils.E2EServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CheckOrderConsistenceManager {

    @Autowired
    private MultiClient serviceOrderConsumerService;

    @Autowired
    private ServiceOrderService serviceOrderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckOrderConsistenceManager.class);

    public ServiceOrderInfo checkServiceOrder(ServiceOrder serviceOrder) {
        ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
        serviceOrderInfo.setServiceOrderId(serviceOrder.getId());
        manageCustomer(serviceOrder, serviceOrderInfo);
        int nbItemsCompleted = 0;

        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            ServiceOrderItemInfo serviceOrderItemInfo = new ServiceOrderItemInfo();
            handleServiceFromCatalog(serviceOrderItem, serviceOrder, serviceOrderItemInfo);
            if (!existServiceInCatalog(serviceOrderItemInfo)) {
                serviceOrderInfo.setIsServiceOrderRejected(true);
                serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem, StateType.REJECTED);
                LOGGER.warn(
                        "service order item {} of service order {} rejected cause no service catalog found for id {}",
                        serviceOrderItem.getId(), serviceOrder.getId(),
                        serviceOrderItem.getService().getServiceSpecification().getId());
                serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "102");
            } else {
                switch (serviceOrderItem.getAction()) {
                    case ADD:
                        handleServiceOrderItemInAdd(serviceOrderInfo, serviceOrder, serviceOrderItem,
                                serviceOrderItemInfo);
                        break;
                    case MODIFY:
                    case DELETE:
                        handleServiceOrderItemInChange(serviceOrderInfo, serviceOrder, serviceOrderItem,
                                serviceOrderItemInfo);
                        break;
                    case NOCHANGE:
                        serviceOrderInfo.setAllItemsInAdd(false);
                        serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem, StateType.COMPLETED);
                        nbItemsCompleted++;
                        break;
                }
            }

        }

        if (serviceOrder.getOrderItem().size() != nbItemsCompleted) {
            serviceOrderInfo.setAllItemsCompleted(false);
        } else {
            serviceOrderInfo.setAllItemsCompleted(true);
        }

        return serviceOrderInfo;

    }

    private void handleServiceOrderItemInChange(ServiceOrderInfo serviceOrderInfo, ServiceOrder serviceOrder,
            ServiceOrderItem serviceOrderItem, ServiceOrderItemInfo serviceOrderItemInfo) {
        serviceOrderInfo.setAllItemsInAdd(false);
        if (shouldAcceptServiceOrderItemToChange(serviceOrderInfo, serviceOrder, serviceOrderItem,
                serviceOrderItemInfo)) {
            serviceOrderInfo.addServiceOrderItemInfos(serviceOrderItem.getId(), serviceOrderItemInfo);
        } else {
            serviceOrderInfo.setIsServiceOrderRejected(true);
            serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem, StateType.REJECTED);
        }
    }

    private void handleServiceOrderItemInAdd(ServiceOrderInfo serviceOrderInfo, ServiceOrder serviceOrder,
            ServiceOrderItem serviceOrderItem, ServiceOrderItemInfo serviceOrderItemInfo) {
        boolean e2eService = E2EServiceUtils.isE2EService(serviceOrderItemInfo);
        if (shouldAcceptServiceOrderItemToAdd(serviceOrderItem, serviceOrder, serviceOrderInfo.getServiceOrderId(),
                e2eService)) {
            serviceOrderInfo.addServiceOrderItemInfos(serviceOrderItem.getId(), serviceOrderItemInfo);
        } else {
            serviceOrderInfo.setIsServiceOrderRejected(true);
            serviceOrderService.updateOrderItemState(serviceOrder, serviceOrderItem, StateType.REJECTED);
        }
    }

    private boolean shouldAcceptServiceOrderItemToAdd(ServiceOrderItem serviceOrderItem, ServiceOrder serviceOrder,
            String serviceOrderId, Boolean e2eService) {
        if (!StringUtils.isEmpty(serviceOrderItem.getService().getId())) {
            LOGGER.warn("serviceOrderItem {} for serviceorder {} rejected cause service.id must be empty in add action",
                    serviceOrderItem.getId(), serviceOrderId);
            serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "103");
            return false;
        } else if (!e2eService && !serviceOrderConsumerService.isTenantIdPresentInAAI(serviceOrder)) {
            LOGGER.warn("serviceOrderItem {}  for serviceOrder {} rejected cause tenantId not found in AAI",
                    serviceOrderItem.getId(), serviceOrderId);
            serviceOrderService.addOrderMessage(serviceOrder, "503");

            return false;
        }
        return true;
    }

    private boolean shouldAcceptServiceOrderItemToChange(ServiceOrderInfo serviceOrderInfo, ServiceOrder serviceOrder,
            ServiceOrderItem serviceOrderItem, ServiceOrderItemInfo serviceOrderItemInfo) {

        if (StringUtils.isEmpty(serviceOrderItem.getService().getId())) {
            LOGGER.warn(
                    "serviceOrderItem {} for serviceOrder {} rejected cause service.id is mandatory in delete/change action",
                    serviceOrderItem.getId(), serviceOrderInfo.getServiceOrderId());
            serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "101");
            return false;
        } else if (!isCustomerFromServiceOrderPresentInInventory(serviceOrderInfo, serviceOrder)) {
            LOGGER.warn("serviceOrderItem {} for serviceOrder {} rejected cause customer not found in inventory",
                    serviceOrderItem.getId(), serviceOrderInfo.getServiceOrderId());
            serviceOrderService.addOrderMessage(serviceOrder, "104");
            return false;
        } else if (!existServiceInInventory(serviceOrderItem, serviceOrder, serviceOrderItemInfo,
                serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId())) {
            LOGGER.warn("serviceOrderItem {} for serviceOrder {} rejected cause service id {} not found in inventory",
                    serviceOrderItem.getId(), serviceOrderInfo.getServiceOrderId(),
                    serviceOrderItem.getService().getId());
            serviceOrderService.addOrderItemMessage(serviceOrder, serviceOrderItem, "106");
            return false;
        }
        return true;
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

    private boolean isCustomerFromServiceOrderPresentInInventory(ServiceOrderInfo serviceOrderInfo,
            ServiceOrder serviceOrder) {

        if (serviceOrderInfo.isUseServiceOrderCustomer()) {
            return serviceOrderConsumerService
                    .isCustomerPresentInAAI(serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId(), serviceOrder);
        }
        return true;
    }

    private boolean existServiceInInventory(ServiceOrderItem serviceOrderItem, ServiceOrder serviceOrder,
            ServiceOrderItemInfo serviceOrderItemInfo, String globalSubscriberId) {
        String serviceName = (String) serviceOrderItemInfo.getCatalogResponse().get("name");
        return serviceOrderConsumerService.doesServiceExistInServiceInventory(serviceOrderItem.getService().getId(),
                serviceName, globalSubscriberId, serviceOrder);
    }

    private boolean existServiceInCatalog(ServiceOrderItemInfo serviceOrderItemInfo) {
        return serviceOrderItemInfo.getCatalogResponse() != null;
    }

    private void handleServiceFromCatalog(ServiceOrderItem serviceOrderItem, ServiceOrder serviceOrder,
            ServiceOrderItemInfo serviceOrderItemInfo) {
        Map body = serviceOrderConsumerService.getServiceCatalog(
                serviceOrderItem.getService().getServiceSpecification().getId(), serviceOrder, serviceOrderItem);
        serviceOrderItemInfo.setCatalogResponse(body);

    }

}
