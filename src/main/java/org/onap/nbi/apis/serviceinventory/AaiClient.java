/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
package org.onap.nbi.apis.serviceinventory;

import java.util.LinkedHashMap;
import org.onap.nbi.OnapComponentsUrlPaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AaiClient extends BaseClient {

    public static final String CUSTOMER_ID = "$customerId";

    @Value("${aai.host}")
    private String aaiHost;

    @Value("${aai.header.authorization}")
    private String aaiHeaderAuthorization;

    @Value("${aai.api.id}")
    private String aaiApiId;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_FROM_APP_ID = "X-FromAppId";

    private HttpHeaders buildRequestHeaderForAAI() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, aaiHeaderAuthorization);
        httpHeaders.add(X_FROM_APP_ID, aaiApiId);
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;

    }

    public LinkedHashMap getCatalogService(String customerId, String serviceSpecName, String serviceId) {

        StringBuilder callURL =
                new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_SERVICE_FOR_CUSTOMER_PATH);
        String callUrlFormated = callURL.toString().replace(CUSTOMER_ID, customerId);
        callUrlFormated = callUrlFormated.replace("$serviceSpecName", serviceSpecName);
        callUrlFormated = callUrlFormated.replace("$serviceId", serviceId);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI());
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            return (LinkedHashMap) response.getBody();
        }
        return null;
    }


    public LinkedHashMap getVNF(String relatedLink) {

        StringBuilder callURL = new StringBuilder().append(aaiHost).append(relatedLink);

        ResponseEntity<Object> response = callApiGet(callURL.toString(), buildRequestHeaderForAAI());
        return (LinkedHashMap) response.getBody();

    }

    public LinkedHashMap getServicesInAaiForCustomer(String customerId) {
        StringBuilder callURL =
                new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_SERVICES_FOR_CUSTOMER_PATH);
        String callUrlFormated = callURL.toString().replace(CUSTOMER_ID, customerId);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI());
        return (LinkedHashMap) response.getBody();
    }

    public LinkedHashMap getServiceInstancesInAaiForCustomer(String customerId, String serviceType) {
        StringBuilder callURL =
                new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_SERVICE_INSTANCES_PATH);
        String callUrlFormated = callURL.toString().replace(CUSTOMER_ID, customerId);
        callUrlFormated = callUrlFormated.replace("$serviceSpecName", serviceType);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI());
        return (LinkedHashMap) response.getBody();
    }
}
