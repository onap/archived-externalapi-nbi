/**
 *     Copyright (c) 2018 Orange
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
package org.onap.nbi.apis.hub.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Configurable
public class NotificationAspect {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private NotifierService notifier;

    @AfterReturning(value = "execution(* org.onap.nbi.apis.serviceorder.service.ServiceOrderService" +
            ".createServiceOrder(..))", returning = "serviceOrderCreated")
    public void whenCreateServiceOrder(ServiceOrder serviceOrderCreated) {
        if(StateType.ACKNOWLEDGED.equals(serviceOrderCreated.getState())) {
            // Notif createServiceOrder
        }
    }

    @AfterReturning(value = "execution(* org.onap.nbi.apis.serviceorder.service.ServiceOrderService" +
            ".updateOrderState(..))", returning = "serviceOrderUpdated")
    public void whenUpdateServiceOrderState(ServiceOrder serviceOrderUpdated) {
        if(StateType.COMPLETED.equals(serviceOrderUpdated.getState())||
                StateType.FAILED.equals(serviceOrderUpdated.getState())) {
            // Notif updateServiceOrder
        }
    }

    @AfterReturning(value = "execution(* org.onap.nbi.apis.serviceorder.service.ServiceOrderService" +
            ".updateOrderItemState(..))")
    public void whenUpdateServiceOrderItemState(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();

        if(signatureArgs != null && signatureArgs.length == 3) {
            StateType serviceOrderItemState = (StateType) signatureArgs[2];
            //  Notif updateServiceOrderItem

        }
    }
}
