package org.onap.nbi.apis.serviceorder.repositories;


import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfoJson;
import org.springframework.data.repository.CrudRepository;

public interface ServiceOrderInfoRepository extends CrudRepository<ServiceOrderInfoJson, String> {

}
