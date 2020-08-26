/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.nbi.apis.servicecatalog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.onap.nbi.apis.servicecatalog.jolt.FindServiceSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.jolt.GetServiceSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.jolt.PostServiceResponseSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.jolt.PostServiceSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.model.ServiceSpecificationRequest;
import org.onap.nbi.apis.serviceorder.ServiceCatalogUrl;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

@Service
public class ServiceSpecificationService {

    @Autowired
    SdcClient sdcClient;

    @Autowired
    GetServiceSpecJsonTransformer getServiceSpecJsonTransformer;

    @Autowired
    FindServiceSpecJsonTransformer findServiceSpecJsonTransformer;

    // Change for processing POST request
    @Autowired
    PostServiceSpecJsonTransformer postServiceSpecJsonTransformer;

    @Autowired
    PostServiceResponseSpecJsonTransformer postServiceResponseSpecJsonTransformer ;

    @Autowired
    ToscaInfosProcessor toscaInfosProcessor;

    @Autowired
    private ServiceCatalogUrl serviceCatalogUrl;

    @Autowired
    ServiceSpecificationDBManager serviceSpecificationDBManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSpecificationService.class);

    public Map get(String serviceSpecId) {
        if (serviceSpecificationDBManager.checkServiceSpecExistence(serviceSpecId)) {
            return serviceSpecificationDBManager.getServiceSpecification(serviceSpecId);
        } else {
            Map sdcResponse = sdcClient.callGet(serviceSpecId);
            LinkedHashMap serviceCatalogResponse = (LinkedHashMap) getServiceSpecJsonTransformer.transform(sdcResponse);
            String toscaModelUrl = (String) sdcResponse.get("toscaModelURL");
            String serviceId = (String) sdcResponse.get("id");
            File toscaFile = sdcClient.callGetWithAttachment(toscaModelUrl);
            Path pathToToscaCsar = toscaFile.toPath().toAbsolutePath();
            try {
                toscaInfosProcessor.buildAndSaveResponseWithSdcToscaParser(pathToToscaCsar, serviceCatalogResponse);
                serviceSpecificationDBManager.saveCatalogResponse(serviceCatalogResponse);
            } catch (SdcToscaParserException e) {
                LOGGER.debug("unable to build response from tosca csar using sdc-parser, partial response : "
                        + pathToToscaCsar.toString() + " " + e.getMessage());
            }
            try {
                if (toscaFile != null) {
                    LOGGER.debug("deleting tosca archive : " + toscaFile.getName());
                    FileUtils.forceDelete(toscaFile);
                }
            } catch (IOException e) {
                LOGGER.error("unable to delete temp directory tosca file for id : " + serviceId, e);
            }
            return serviceCatalogResponse;
        }
    }

    public List<LinkedHashMap> find(MultiValueMap<String, String> parametersMap) {
        List<LinkedHashMap> sdcResponse = sdcClient.callFind(parametersMap);
        List<LinkedHashMap> serviceCatalogResponse = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sdcResponse)) {
            serviceCatalogResponse = findServiceSpecJsonTransformer.transform(sdcResponse);
        }
        return serviceCatalogResponse;
    }

    public String getInputSchema(String serviceSpecId) {
        if (serviceSpecificationDBManager.checkInputSchemaExistence(serviceSpecId)) {
            return serviceSpecificationDBManager.getInputSchema(serviceSpecId);
        } else {
            return null;
        }
    }

    public Map create(String userId, ServiceSpecificationRequest specRequest) {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap specRequestMap = mapper.convertValue(specRequest, LinkedHashMap.class);
        HashMap<Object, Object> serviceCatalogInput = (HashMap) postServiceSpecJsonTransformer.transform(specRequestMap);

        //Call SDC Post API
        Map sdcResponse = sdcClient.callPost(serviceCatalogInput,userId);
        LOGGER.info("SDC response " + sdcResponse);
        //Transform SDC Response
        LinkedHashMap<Object,Object> serviceCatalogResponse =null;
        if (!CollectionUtils.isEmpty(sdcResponse)) {
            serviceCatalogResponse = (LinkedHashMap)postServiceResponseSpecJsonTransformer.transform(sdcResponse);
        }
        return serviceCatalogResponse;
    }

}
