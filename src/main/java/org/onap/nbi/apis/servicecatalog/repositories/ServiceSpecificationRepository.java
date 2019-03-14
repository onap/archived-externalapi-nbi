package org.onap.nbi.apis.servicecatalog.repositories;

import org.onap.nbi.apis.servicecatalog.model.ServiceSpecification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceSpecificationRepository extends MongoRepository<ServiceSpecification, String> {
}
