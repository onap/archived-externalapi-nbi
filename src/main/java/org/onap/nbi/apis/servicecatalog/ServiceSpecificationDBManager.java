package org.onap.nbi.apis.servicecatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.nbi.apis.servicecatalog.model.ServiceSpecification;
import org.onap.nbi.apis.servicecatalog.model.SpecificationInputSchema;
import org.onap.nbi.apis.servicecatalog.repositories.ServiceSpecificationRepository;
import org.onap.nbi.apis.servicecatalog.repositories.SpecificationInputSchemaRepository;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceSpecificationDBManager {

    @Autowired
    ServiceSpecificationRepository serviceSpecificationRepository;

    @Autowired
    SpecificationInputSchemaRepository specificationInputSchemaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSpecificationService.class);

    public void saveCatalogResponse(LinkedHashMap serviceCatalogResponse) {

        ServiceSpecification serviceSpecification = new ServiceSpecification();
        serviceSpecification.setId((String) serviceCatalogResponse.get("id"));
        serviceSpecification.setCatalogResponse(serviceCatalogResponse);
        serviceSpecificationRepository.save(serviceSpecification);

   }

    public boolean checkServiceSpecExistence(String serviceSpecId) {

        return serviceSpecificationRepository.existsById(serviceSpecId);
    }

    public Map getServiceSpecification(String serviceSpecId) {

       Optional<ServiceSpecification> optionalServiceSpecification = serviceSpecificationRepository.findById(serviceSpecId);
       if(!optionalServiceSpecification.isPresent()) {
           throw new TechnicalException("Unable get service specification");
       }else {
           return  optionalServiceSpecification.get().getCatalogResponse();
       }
    }

    public boolean checkInputSchemaExistence(String serviceSpecId) {
        return  specificationInputSchemaRepository.existsById(serviceSpecId);
    }

    public String getInputSchema(String serviceSpecId) {
        Optional<SpecificationInputSchema> optionalSpecificationInputSchema = specificationInputSchemaRepository.findById(serviceSpecId);
        if(!optionalSpecificationInputSchema.isPresent()) {
            throw new TechnicalException("Unable get specification input schema");
        }else {
            return  optionalSpecificationInputSchema.get().getSpecificationSchemaJson();
        }
    }

    public void saveSpecificationInputSchema(String svcCharacteristicsJson, Map serviceCatalogResponse) {
            ObjectMapper mapper = new ObjectMapper();
            SpecificationInputSchema specificationInputSchema = new SpecificationInputSchema();
            specificationInputSchema.setId((String) serviceCatalogResponse.get("id"));
            specificationInputSchema.setSpecificationSchemaJson(svcCharacteristicsJson);
            specificationInputSchemaRepository.save(specificationInputSchema);

    }
}
