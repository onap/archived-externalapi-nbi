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

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.onap.nbi.OnapComponentsUrlPaths;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Value("${aai.header.transaction.id}")
    private String aaiTransactionId;


    @Value("${onap.lcpCloudRegionId}")
    private String lcpCloudRegionId;

    @Value("${onap.tenantId}")
    private String tenantId;

    @Value("${onap.cloudOwner}")
    private String cloudOwner;

    @Value("${so.owning.entity.id}")
    private String owningEntityId;

    @Value("${so.owning.entity.name}")
    private String owningEntityName;

    @Autowired
    private ServiceCatalogUrl serviceCatalogUrl;

    @Autowired
    private ServiceInventoryUrl serviceInventoryUrl;

    @Autowired
    private ServiceOrderService serviceOrderService;


    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_FROM_APP_ID = "X-FromAppId";
    private static final String X_TRANSACTION_ID = "X-TransactionId";

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiClient.class);

    public Map getServiceCatalog(String id,ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem){
        StringBuilder callURL = new StringBuilder().append(serviceCatalogUrl.getServiceCatalogUrl()).append(id);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders(), null);

        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "500");
            LOGGER.warn("unable to retrieve catalog information for service {}",
                serviceOrderItem.getService().getServiceSpecification().getId());
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            return (LinkedHashMap) response.getBody();
        }
        return null;

    }

    public boolean doesServiceExistInServiceInventory(String id, String serviceName, String globalSubscriberId, ServiceOrder serviceOrder) {
        StringBuilder callURL = new StringBuilder().append(serviceInventoryUrl.getServiceInventoryUrl()).append(id);
        Map<String, String> param = new HashMap<>();
        param.put("serviceSpecification.name", serviceName);
        param.put("relatedParty.id", globalSubscriberId);

        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders(), param);
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
            return false;
        }
        return response.getStatusCode().equals(HttpStatus.OK);
    }


    private HttpHeaders buildRequestHeaderForAAI() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, aaiHeaderAuthorization);
        httpHeaders.add(X_FROM_APP_ID, aaiApiId);
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add(X_TRANSACTION_ID, aaiTransactionId);

        return httpHeaders;
    }


    public boolean isTenantIdPresentInAAI(ServiceOrder serviceOrder) {
        StringBuilder callURL = new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_TENANTS_PATH);
        String callUrlFormated = callURL.toString().replace("$onap.lcpCloudRegionId", lcpCloudRegionId);
        callUrlFormated = callUrlFormated.replace("$onap.cloudOwner", cloudOwner);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI(), null);
        if (response.getStatusCode().is2xxSuccessful()) {
            LinkedHashMap body = (LinkedHashMap) response.getBody();
            List<LinkedHashMap> tenants = (List<LinkedHashMap>) body.get("tenant");
            for (LinkedHashMap tenant : tenants) {
                if (tenantId.equalsIgnoreCase((String) tenant.get("tenant-id"))) {
                    return true;
                }
            }
        } else {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
        }
        return false;
    }


    public String getOwningEntityIdInAAI(ServiceOrder serviceOrder) {
        StringBuilder callURL = new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_OWNING_ENTITIES);
        String callUrlFormated = callURL.toString();

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI(), null);
        if (response.getStatusCode().is2xxSuccessful()) {
            LinkedHashMap body = (LinkedHashMap) response.getBody();
            List<LinkedHashMap> owningEntities = (List<LinkedHashMap>) body.get("owning-entity");
            for (LinkedHashMap owningEntity : owningEntities) {
                if (owningEntityName.equalsIgnoreCase((String) owningEntity.get("owning-entity-name"))) {
                    return owningEntity.get("owning-entity-id").toString();
                }
            }
        } else {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
        }
        return null;
    }


    public boolean isCustomerPresentInAAI(String customerId,
        ServiceOrder serviceOrder) {
        StringBuilder callURL = new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_CUSTOMER_PATH)
                .append(customerId);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), buildRequestHeaderForAAI(), null);
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
            return false;
        }
        return response.getStatusCode().equals(HttpStatus.OK);
    }


    public boolean putOwningEntity(ServiceOrder serviceOrder) {
        Map<String, String> param = new HashMap<>();
        param.put("owning-entity-id", owningEntityId);
        param.put("owning-entity-name", owningEntityName);
        String callURL =
            aaiHost + OnapComponentsUrlPaths.AAI_PUT_OWNING_ENTITIES;
        String callUrlFormated = callURL.replace("$onap.owning.entity.id", owningEntityId);
        ResponseEntity<Object> response = putRequest(param, callUrlFormated, buildRequestHeaderForAAI());
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
            return false;
        }
        return response.getStatusCode().equals(HttpStatus.CREATED);
    }


    public boolean putCustomer(SubscriberInfo subscriberInfo,
        ServiceOrder serviceOrder) {
        Map<String, String> param = new HashMap<>();
        param.put("global-customer-id", subscriberInfo.getGlobalSubscriberId());
        param.put("subscriber-name", subscriberInfo.getSubscriberName());
        param.put("subscriber-type", "BSS");
        String callURL =
                aaiHost + OnapComponentsUrlPaths.AAI_GET_CUSTOMER_PATH + subscriberInfo.getGlobalSubscriberId();

        ResponseEntity<Object> response = putRequest(param, callURL, buildRequestHeaderForAAI());
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
            return false;
        }
        return response.getStatusCode().equals(HttpStatus.CREATED);
    }


    public Map getServicesInAaiForCustomer(String customerId,
        ServiceOrder serviceOrder) {
        StringBuilder callURL =
                new StringBuilder().append(aaiHost).append(OnapComponentsUrlPaths.AAI_GET_SERVICES_FOR_CUSTOMER_PATH);
        String callUrlFormated = callURL.toString().replace("$customerId", customerId);

        ResponseEntity<Object> response = callApiGet(callUrlFormated, buildRequestHeaderForAAI(), null);
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
            return null;
        }
        else if (response.getStatusCode().is2xxSuccessful()) {
            return (LinkedHashMap) response.getBody();
        }
        return null;
    }

    public boolean putServiceType(String globalSubscriberId, String serviceName,
        ServiceOrder serviceOrder) {
        Map<String, String> param = new HashMap<>();
        param.put("service-type", serviceName);
        String callURL = aaiHost + OnapComponentsUrlPaths.AAI_PUT_SERVICE_FOR_CUSTOMER_PATH + serviceName;
        String callUrlFormated = callURL.replace("$customerId", globalSubscriberId);
        ResponseEntity<Object> response =  putRequest(param, callUrlFormated, buildRequestHeaderForAAI());
        if(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            serviceOrderService.addOrderMessage(serviceOrder, "501");
        }
        return response.getStatusCode().is2xxSuccessful();
    }


    private  ResponseEntity<Object> putRequest(Map<String, String> param, String callUrl, HttpHeaders httpHeaders) {
        try {
            ResponseEntity<Object> response =
                    restTemplate.exchange(callUrl, HttpMethod.PUT, new HttpEntity<>(param, httpHeaders), Object.class);
            LOGGER.info("response status : " + response.getStatusCodeValue());
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.CREATED)) {
                LOGGER.warn("HTTP call on {} returns {} , {}", callUrl , response.getStatusCodeValue(), response.getBody().toString());
            }
            return response;
        } catch (BackendFunctionalException e) {
            LOGGER.error("error on calling " + callUrl + " ," + e);
            return new ResponseEntity<>("problem calling onap services", e.getHttpStatus());
        }catch (ResourceAccessException e) {
            LOGGER.error("error on calling " + callUrl + " ," + e);
            return new ResponseEntity<>("unable to reach onap services", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> callApiGet(String callURL, HttpHeaders httpHeaders, Map<String, String> param) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(callURL);
            if (param != null) {
                for (Entry<String, String> stringEntry : param.entrySet()) {
                    builder.queryParam(stringEntry.getKey(), stringEntry.getValue());

                }
            }
            URI uri = builder.build().encode().toUri();
            ResponseEntity<Object> response =
                    restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), Object.class);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("response body : {}", response.getBody().toString());
            }
            LOGGER.info("response status : {}", response.getStatusCodeValue());
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.warn("HTTP call on {} returns {} , {}", callURL , response.getStatusCodeValue(), response.getBody().toString());

            }
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error("error on calling " + callURL + " ," + e);
            return new ResponseEntity<>("problem calling onap services", e.getHttpStatus());
        }catch (ResourceAccessException e) {
            LOGGER.error("error on calling " + callURL + " ," + e);
            return new ResponseEntity<>("unable to reach onap services", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}