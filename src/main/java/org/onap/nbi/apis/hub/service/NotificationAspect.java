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
import org.onap.nbi.apis.hub.model.Event;
import org.onap.nbi.apis.hub.model.EventType;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.onap.nbi.exceptions.TechnicalException;

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
            processEvent(EventFactory.getEvent(EventType.SERVICE_ORDER_CREATION, serviceOrderCreated, null));
        }
    }

    @AfterReturning(value = "execution(* org.onap.nbi.apis.serviceorder.service.ServiceOrderService" +
            ".updateOrderState(..))", returning = "serviceOrderUpdated")
    public void whenUpdateServiceOrderState(ServiceOrder serviceOrderUpdated) {
        if(StateType.COMPLETED.equals(serviceOrderUpdated.getState())||
                StateType.FAILED.equals(serviceOrderUpdated.getState())) {
            processEvent(EventFactory.getEvent(EventType.SERVICE_ORDER_STATE_CHANGE, serviceOrderUpdated, null));
        }
    }

    @AfterReturning(value = "execution(* org.onap.nbi.apis.serviceorder.service.ServiceOrderService" +
            ".updateOrderItemState(..))")
    public void whenUpdateServiceOrderItemState(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();

        if(signatureArgs != null && signatureArgs.length == 3) {
            ServiceOrder serviceOrder = (ServiceOrder) signatureArgs[0];
            ServiceOrderItem serviceOrderItem = (ServiceOrderItem) signatureArgs[1];
            StateType serviceOrderItemState = (StateType) signatureArgs[2];

            processEvent(EventFactory.getEvent(EventType.SERVICE_ORDER_ITEM_STATE_CHANGE, serviceOrder,
                    serviceOrderItem));
        }
    }

    public void forwardNotificationToOriginalListener(Event event) {
        if(event != null) {
            processEvent(event);
        }else{
            throw new TechnicalException("Received null event from external NBI");
        }
    }

    /**
     * Retreive subscribers that match an event and fire notification
     * asynchronously
     * @param event
     */
    private void processEvent(Event event) {
        subscriberRepository
                .findSubscribersUsingEvent(event)
                .forEach(sub -> notifier.run(sub, event));
    }
}
