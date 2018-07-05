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
package org.onap.nbi.apis.servicecatalog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.onap.nbi.apis.servicecatalog.jolt.FindServiceSpecJsonTransformer;
import org.onap.nbi.apis.servicecatalog.jolt.GetServiceSpecJsonTransformer;
import org.onap.nbi.apis.serviceorder.ServiceCatalogUrl;
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

    @Autowired
    private ServiceCatalogUrl serviceCatalogUrl;

    private static final String SERVICE_SPEC_INPUT_SCHEMA = "serviceSpecInputSchema";


    public Map get(String serviceSpecId) {
        Map sdcResponse = sdcClient.callGet(serviceSpecId);
        sdcResponse.put(SERVICE_SPEC_INPUT_SCHEMA,serviceCatalogUrl.getServiceCatalogUrl()+serviceSpecId+"/"+SERVICE_SPEC_INPUT_SCHEMA);
        return (LinkedHashMap) getServiceSpecJsonTransformer.transform(sdcResponse);
    }


    public List<LinkedHashMap> find(MultiValueMap<String, String> parametersMap) {
        List<LinkedHashMap> sdcResponse = sdcClient.callFind(parametersMap);
        List<LinkedHashMap> serviceCatalogResponse = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(sdcResponse)){
            serviceCatalogResponse =
                findServiceSpecJsonTransformer.transform(sdcResponse);
        }
        return serviceCatalogResponse;
    }
}
