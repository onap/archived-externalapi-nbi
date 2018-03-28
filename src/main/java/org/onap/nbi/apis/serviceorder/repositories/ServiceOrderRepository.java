package org.onap.nbi.apis.serviceorder.repositories;

import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ServiceOrderRepository extends MongoRepository<ServiceOrder, String> {

    List<ServiceOrder> findByState(StateType state);
}
