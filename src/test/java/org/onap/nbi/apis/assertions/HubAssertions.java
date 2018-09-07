package org.onap.nbi.apis.assertions;

import org.onap.nbi.apis.hub.model.Subscription;

public class HubAssertions {

    public static Subscription createServiceOrderCreationSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost:8090");
        subscription.setQuery("eventType = ServiceOrderCreationNotification");
        return subscription;
    }

    public static Subscription createServiceOrderStateChangeSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost:8090");
        subscription.setQuery("eventType = ServiceOrderStateChangeNotification");
        return subscription;
    }

    public static Subscription createServiceOrderItemStateChangeSubscription(){
        Subscription subscription = new Subscription();
        subscription.setId("id");
        subscription.setCallback("http://localhost:8090");
        subscription.setQuery("eventType = ServiceOrderItemStateChangeNotification");
        return subscription;
    }
}
