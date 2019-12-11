/**
 * Copyright (c) 2019 Orange
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateAAIOwningEntityManager {


    @Autowired
    private MultiClient serviceOrderConsumerService;

    @Autowired
    ServiceOrderService serviceOrderService;

    @Value("${so.owning.entity.id}")
    private String owningEntityId;


    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAAIOwningEntityManager.class);


    public void createAAIOwningEntity(ServiceOrder serviceOrder,
        ServiceOrderInfo serviceOrderInfo) {

        String owningEntityIdToSo=serviceOrderConsumerService.getOwningEntityIdInAAI(serviceOrder);
        if (owningEntityIdToSo==null) {
            owningEntityIdToSo=owningEntityId;
            boolean owningEntity = serviceOrderConsumerService.putOwningEntity(serviceOrder);
            if (!owningEntity) {
                serviceOrderService.updateOrderState(serviceOrder, StateType.REJECTED);
                LOGGER.warn("serviceOrder {} rejected : cannot create owning entity ", serviceOrder.getId());
                serviceOrderService.addOrderMessage(serviceOrder, "501");
            }
        }
        serviceOrderInfo.setOwningEntityId(owningEntityIdToSo);
    }


}


