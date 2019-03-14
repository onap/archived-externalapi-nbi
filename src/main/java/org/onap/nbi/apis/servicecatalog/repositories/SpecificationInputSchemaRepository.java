package org.onap.nbi.apis.servicecatalog.repositories;

import org.onap.nbi.apis.servicecatalog.model.SpecificationInputSchema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecificationInputSchemaRepository extends MongoRepository<SpecificationInputSchema, String> {


}
