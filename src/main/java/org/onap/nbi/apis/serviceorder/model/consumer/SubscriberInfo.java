package org.onap.nbi.apis.serviceorder.model.consumer;

public class SubscriberInfo {

    private String globalSubscriberId;

    private String subscriberName;

    public String getGlobalSubscriberId() {
        return globalSubscriberId;
    }

    public void setGlobalSubscriberId(String globalSubscriberId) {
        this.globalSubscriberId = globalSubscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    @Override
    public String toString() {
        return "SubscriberInfo{" + "globalSubscriberId='" + globalSubscriberId + '\'' + ", subscriberName='"
                + subscriberName + '\'' + '}';
    }
}
