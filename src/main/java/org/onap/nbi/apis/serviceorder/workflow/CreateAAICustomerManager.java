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
import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAAICustomerManager {


    @Autowired
    private MultiClient serviceOrderConsumerService;

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAAICustomerManager.class);



    public void createAAICustomer(ServiceOrder serviceOrder,
        ServiceOrderInfo serviceOrderInfo) {

        if (serviceOrderInfo.isUseServiceOrderCustomer() && serviceOrderInfo.isAllItemsInAdd()
            && !serviceOrderConsumerService
            .isCustomerPresentInAAI(serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId())) {

            boolean customerCreated = serviceOrderConsumerService.putCustomer(serviceOrderInfo.getSubscriberInfo());
            if (!customerCreated) {
                serviceOrder.setState(StateType.REJECTED);
                serviceOrder.setCompletionDateTime(new Date());
                serviceOrderRepository.save(serviceOrder);
                LOGGER.warn("serviceOrder {} rejected : cannot create customer", serviceOrder.getId());
            }
        }
    }


}


