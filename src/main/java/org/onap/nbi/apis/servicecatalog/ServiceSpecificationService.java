package org.onap.nbi.apis.servicecatalog;

import java.util.LinkedHashMap;
import java.util.List;
import org.onap.nbi.apis.servicecatalog.jolt.FindServiceSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.jolt.GetServiceSpecJsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class ServiceSpecificationService {

    @Autowired
    SdcClient sdcClient;

    @Autowired
    GetServiceSpecJsonTransformer getServiceSpecJsonTransformer;

    @Autowired
    FindServiceSpecJsonTransformer findServiceSpecJsonTransformer;

    @Autowired
    ToscaInfosProcessor toscaInfosProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSpecificationService.class);


    public LinkedHashMap get(String serviceSpecId) {
        LinkedHashMap sdcResponse = sdcClient.callGet(serviceSpecId);
        LinkedHashMap serviceCatalogResponse = (LinkedHashMap) getServiceSpecJsonTransformer.transform(sdcResponse);
        LinkedHashMap toscaInfosTopologyTemplate = toscaInfosProcessor.getToscaInfos(serviceCatalogResponse);
        if (toscaInfosTopologyTemplate != null) {
            LOGGER.debug("tosca file found, retrieving informations");
            toscaInfosProcessor.buildResponseWithToscaInfos(toscaInfosTopologyTemplate, serviceCatalogResponse);
        } else {
            LOGGER.debug("no tosca file found, partial response");
        }
        return serviceCatalogResponse;
    }


    public List<LinkedHashMap> find(MultiValueMap<String, String> parametersMap) {
        List<LinkedHashMap> sdcResponse = sdcClient.callFind(parametersMap);
        List<LinkedHashMap> serviceCatalogResponse =
                (List<LinkedHashMap>) findServiceSpecJsonTransformer.transform(sdcResponse);
        return serviceCatalogResponse;
    }
}
