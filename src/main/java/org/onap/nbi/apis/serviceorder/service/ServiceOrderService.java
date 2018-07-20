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
package org.onap.nbi.apis.serviceorder.service;

import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceOrderService {

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    public ServiceOrder findServiceOrderById(String serviceOrderId){
        return serviceOrderRepository.findOne(serviceOrderId);
    }

    public List<ServiceOrder> findServiceOrdersByState(StateType state){
        return serviceOrderRepository.findByState(state);
    }

    public ServiceOrder updateOrderState(ServiceOrder serviceOrder,StateType state){
        if(StateType.COMPLETED.equals(state) || StateType.REJECTED.equals(state)) {
            serviceOrder.setCompletionDateTime(new Date());
        }
        serviceOrder.setState(state);
        return serviceOrderRepository.save(serviceOrder);
    }

    public void updateOrderItemState(ServiceOrder serviceOrder,ServiceOrderItem serviceOrderItem, StateType state){
        serviceOrderItem.setState(state);
        serviceOrderRepository.save(serviceOrder);
    }

    public ServiceOrder createServiceOrder(ServiceOrder serviceOrder){
        serviceOrder.setState(StateType.ACKNOWLEDGED);
        serviceOrder.setOrderDate(new Date());
        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            serviceOrderItem.setState(StateType.ACKNOWLEDGED);
        }
        serviceOrder = serviceOrderRepository.save(serviceOrder);
        serviceOrder.setHref("serviceOrder/" + serviceOrder.getId());
        return serviceOrderRepository.save(serviceOrder);
    }

    public void deleteServiceOrder(String serviceOrderId){
        serviceOrderRepository.delete(serviceOrderId);
    }

    public long countServiceOrder(){
        return serviceOrderRepository.count();
    }


}
