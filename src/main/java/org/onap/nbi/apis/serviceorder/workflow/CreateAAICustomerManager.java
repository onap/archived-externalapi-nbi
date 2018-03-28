package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.MultiClient;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAAICustomerManager {


    @Autowired
    private MultiClient serviceOrderConsumerService;


    public void createAAICustomer(ServiceOrderInfo serviceOrderInfo) {


        if (serviceOrderInfo.isUseServiceOrderCustomer() && serviceOrderInfo.isAllItemsInAdd()
                && !serviceOrderConsumerService
                        .isCustomerPresentInAAI(serviceOrderInfo.getSubscriberInfo().getGlobalSubscriberId())) {
            serviceOrderConsumerService.putCustomer(serviceOrderInfo.getSubscriberInfo());
        }
    }


}


