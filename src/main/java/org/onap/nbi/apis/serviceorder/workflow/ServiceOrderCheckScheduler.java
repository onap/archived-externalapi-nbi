package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.ServiceOrderResource;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("prod")
@Service
@EnableScheduling
public class ServiceOrderCheckScheduler {

    @Autowired
    ServiceOrderService serviceOrderService;

    @Autowired
    ServiceOrderResource serviceOrderResource;


    @Scheduled(fixedDelayString = "${serviceOrder.schedule}", initialDelayString = "${serviceOrder.initial}")
    public void scheduleCheckServiceOrders() {
        List<ServiceOrder> acknowledgedOrders = serviceOrderService.findServiceOrdersByState(StateType.ACKNOWLEDGED);
        for (ServiceOrder serviceOrder : acknowledgedOrders) serviceOrderResource.checkServiceOrder(serviceOrder);
    }

}
