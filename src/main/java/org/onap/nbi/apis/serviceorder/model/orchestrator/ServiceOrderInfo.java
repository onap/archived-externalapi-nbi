package org.onap.nbi.apis.serviceorder.model.orchestrator;


import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;

import java.util.HashMap;
import java.util.Map;

public class ServiceOrderInfo {

    private boolean useServiceOrderCustomer;
    private SubscriberInfo subscriberInfo;
    private Map<String, ServiceOrderItemInfo> serviceOrderItemInfos = new HashMap<>();
    private boolean allItemsInAdd;
    private boolean allItemsCompleted;
    private boolean serviceOrderRejected;

    public boolean isAllItemsInAdd() {
        return allItemsInAdd;
    }

    public void setAllItemsInAdd(boolean allItemsInAdd) {
        this.allItemsInAdd = allItemsInAdd;
    }

    public boolean isAllItemsCompleted() {
        return allItemsCompleted;
    }

    public void setAllItemsCompleted(boolean allItemsCompleted) {
        this.allItemsCompleted = allItemsCompleted;
    }

    public boolean isServiceOrderRejected() {
        return serviceOrderRejected;
    }

    public void setIsServiceOrderRejected(boolean isServiceOrderRejected) {
        this.serviceOrderRejected = isServiceOrderRejected;
    }

    public Map<String, ServiceOrderItemInfo> getServiceOrderItemInfos() {
        return serviceOrderItemInfos;
    }

    public void addServiceOrderItemInfos(String id, ServiceOrderItemInfo serviceOrderItemInfo) {
        this.serviceOrderItemInfos.put(id, serviceOrderItemInfo);
    }

    public SubscriberInfo getSubscriberInfo() {
        return subscriberInfo;
    }

    public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }


    public boolean isUseServiceOrderCustomer() {
        return useServiceOrderCustomer;
    }

    public void setUseServiceOrderCustomer(boolean useServiceOrderCustomer) {
        this.useServiceOrderCustomer = useServiceOrderCustomer;
    }


}
