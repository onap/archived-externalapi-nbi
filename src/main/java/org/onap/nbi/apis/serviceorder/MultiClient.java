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
package org.onap.nbi.apis.serviceorder;

import org.onap.nbi.OnapComponentsUrlPaths;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MultiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aai.host}")
    private String aaiHost;

    @Value("${aai.header.authorization}")
    private String aaiHeaderAuthorization;

    @Value("${aai.api.id}")
    private String aaiApiId;

    @Value("${onap.lcpCloudRegionId}")
    private String lcpCloudRegionId;

    @Value("${onap.tenantId}")
    private String tenantId;

    @Value("${onap.cloudOwner}")
    private String cloudOwner;

    @Autowired
    private ServiceCatalogUrl serviceCatalogUrl;

    @Autowired
    private ServiceInventoryUrl serviceInventoryUrl;


    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_FROM_APP_ID = "X-FromAppId";

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiClient.class);

    public ResponseEntity<Object> getServiceCatalog(String id) {
        StringBuilder callURL = new StringBuilder().append(serviceCatalogUrl.getServiceCatalogUrl()).append(id);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders(), null);
        return response;
    }

    public boolean doesServiceExistInServiceInventory(String id, String serviceName, String globalSubscriberId) {
        StringBuilder callURL = new StringBuilder().append(serviceInventoryUrl.getServiceInventoryUrl()).append(id);
        Map<String, String> param = new HashMap<>();
        param.put("serviceSpecification.name", serviceName);
        param.put("relatedParty.id", globalSubscriberId);

        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders(), param);
        if (response == null || !response.getStatusCode().equals(HttpStatus.OK)) {
            return false;
        }
        return true;
    }


    private HttpHeaders buildRequestHeaderForAAI() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, aaiHeaderAuthorization);
        httpHeaders.add(X_FROM_APP_ID, aaiApiId);
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;
    }


    public boolean isTenantIdPresentInAAI() {
        StringBuilder callURL = new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_TENANTS_PATH);
        String callUrlFormated = callURL.toString().replace("$onap.lcpCloudRegionId", lcpCloudRegionId);
        callUrlFormated = callUrlFormated.replace("$onap.cloudOwner", cloudOwner);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI(), null);
        if (response != null) {
            LinkedHashMap body = (LinkedHashMap) response.getBody();
            List<LinkedHashMap> tenants = (List<LinkedHashMap>) body.get("tenant");
            for (LinkedHashMap tenant : tenants) {
                if (tenantId.equalsIgnoreCase((String) tenant.get("tenant-id"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCustomerPresentInAAI(String customerId) {
        StringBuilder callURL = new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_CUSTOMER_PATH)
                .append(customerId);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), buildRequestHeaderForAAI(), null);
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            return true;
        }
        return false;
    }


    public boolean putCustomer(SubscriberInfo subscriberInfo) {
        Map<String, String> param = new HashMap<>();
        param.put("global-customer-id", subscriberInfo.getGlobalSubscriberId());
        param.put("subscriber-name", subscriberInfo.getSubscriberName());
        param.put("subscriber-type", "BSS");
        String callURL =
                aaiHost + OnapComponentsUrlPaths.AAI_GET_CUSTOMER_PATH + subscriberInfo.getGlobalSubscriberId();

        ResponseEntity<Object> response = putRequest(param, callURL, buildRequestHeaderForAAI());
        if (response != null && response.getStatusCode().equals(HttpStatus.CREATED)) {
            return true;
        }
        return false;
    }


    public LinkedHashMap getServicesInAaiForCustomer(String customerId) {
        StringBuilder callURL =
                new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_SERVICES_FOR_CUSTOMER_PATH);
        String callUrlFormated = callURL.toString().replace("$customerId", customerId);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI(), null);
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            return (LinkedHashMap) response.getBody();
        }
        return null;
    }

    public boolean putServiceType(String globalSubscriberId, String serviceName) {
        Map<String, String> param = new HashMap<>();
        param.put("service-type", serviceName);
        String callURL = aaiHost + OnapComponentsUrlPaths.AAI_PUT_SERVICE_FOR_CUSTOMER_PATH + serviceName;
        String callUrlFormated = callURL.replace("$customerId", globalSubscriberId);
        ResponseEntity<Object> response =  putRequest(param, callUrlFormated, buildRequestHeaderForAAI());
        if (response != null && response.getStatusCode().equals(HttpStatus.CREATED)) {
            return true;
        }
        return false;
    }


    private  ResponseEntity<Object> putRequest(Map<String, String> param, String callUrl, HttpHeaders httpHeaders) {
        try {
            ResponseEntity<Object> response =
                    restTemplate.exchange(callUrl, HttpMethod.PUT, new HttpEntity<>(param, httpHeaders), Object.class);
            LOGGER.info("response status : " + response.getStatusCodeValue());
            if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
                LOGGER.warn("HTTP call on " + callUrl + " returns " + response.getStatusCodeValue() + ", "
                        + response.getBody().toString());
            }
            return response;
        } catch (BackendFunctionalException e) {
            LOGGER.error("error on calling " + callUrl + " ," + e);
            return null;
        }
    }

    private ResponseEntity<Object> callApiGet(String callURL, HttpHeaders httpHeaders, Map<String, String> param) {


        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(callURL);
            if (param != null) {
                for (String paramName : param.keySet()) {
                    builder.queryParam(paramName, param.get(paramName));
                }
            }
            URI uri = builder.build().encode().toUri();


            ResponseEntity<Object> response =
                    restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), Object.class);
            LOGGER.debug("response body : " + response.getBody().toString());
            LOGGER.info("response status : " + response.getStatusCodeValue());
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.error("HTTP call on " + callURL + " returns " + response.getStatusCodeValue() + ", "
                        + response.getBody().toString());
            }
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error("error on calling " + callURL + " ," + e);
            return null;
        }
    }


}