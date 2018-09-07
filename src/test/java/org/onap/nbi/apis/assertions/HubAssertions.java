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
package org.onap.nbi.apis.assertions;

import org.onap.nbi.apis.hub.model.Event;
import org.onap.nbi.apis.hub.model.EventType;
import org.onap.nbi.apis.hub.model.Subscription;

import java.util.Date;
import java.util.UUID;

public class HubAssertions {

    public static Subscription createServiceOrderCreationSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost:8080/test");
        subscription.setQuery("eventType = ServiceOrderCreationNotification");
        return subscription;
    }

    public static Subscription createServiceOrderStateChangeSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost/test");
        subscription.setQuery("eventType = ServiceOrderStateChangeNotification");
        return subscription;
    }

    public static Subscription createServiceOrderItemStateChangeSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost/test");
        subscription.setQuery("eventType = ServiceOrderItemStateChangeNotification");
        return subscription;
    }

    public static Event createFakeEvent() {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventDate(new Date());
        event.setEventType(EventType.SERVICE_ORDER_CREATION.value());
        return event;
    }
}
