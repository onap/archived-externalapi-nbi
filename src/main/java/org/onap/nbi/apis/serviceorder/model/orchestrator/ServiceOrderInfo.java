/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
