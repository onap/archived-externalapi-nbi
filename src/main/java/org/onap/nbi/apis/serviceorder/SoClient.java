/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.onap.nbi.apis.serviceorder;

import org.onap.nbi.OnapComponentsUrlPaths;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateE2EServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.DeleteE2EServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.GetE2ERequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.GetRequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.MSODeleteE2EPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.MSOE2EPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.MSOPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.ServiceResponse;
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

@Service
public class SoClient {

    public static final String RESPONSE_STATUS = "response status : ";
    public static final String RETURNS = " returns ";
    public static final String ERROR_ON_CALLING = "error on calling ";
    @Autowired
    private RestTemplate restTemplate;

    @Value("${so.host}")
    private String soHostname;

    @Value("${so.api.id}")
    private String soApiId;

    @Value("${so.header.authorization}")
    private String soHeaderAuthorization;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_FROM_APP_ID = "X-FromAppId";

    private static final Logger LOGGER = LoggerFactory.getLogger(SoClient.class);


    public ResponseEntity<CreateServiceInstanceResponse> callCreateServiceInstance(MSOPayload msoPayload) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO CreateServiceInstance with msoPayload : " + msoPayload.toString());
        }

        String url = soHostname + OnapComponentsUrlPaths.MSO_CREATE_SERVICE_INSTANCE_PATH;

        try {
            ResponseEntity<CreateServiceInstanceResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(msoPayload, buildRequestHeader()), CreateServiceInstanceResponse.class);

            logResponsePost(url, response);
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity(e.getBodyResponse(),e.getHttpStatus());
        } catch (ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<CreateE2EServiceInstanceResponse> callE2ECreateServiceInstance(MSOE2EPayload msoPayloadE2E) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO CreateServiceInstanceE2E with msoPayload : " + msoPayloadE2E.toString());
        }

        String url = soHostname + OnapComponentsUrlPaths.MSO_CREATE_E2ESERVICE_INSTANCE_PATH;

        try {
            ResponseEntity<CreateE2EServiceInstanceResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(msoPayloadE2E, buildRequestHeader()), CreateE2EServiceInstanceResponse.class);

            logE2EResponsePost(url, response);
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity(e.getBodyResponse(),e.getHttpStatus());
        } catch (ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CreateServiceInstanceResponse> callDeleteServiceInstance(MSOPayload msoPayload, String serviceId) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO DeleteServiceInstance with msoPayload : " + msoPayload.toString());
        }

        String url = soHostname + OnapComponentsUrlPaths.MSO_DELETE_REQUEST_STATUS_PATH + serviceId;

        try {
            ResponseEntity<CreateServiceInstanceResponse> response = restTemplate.exchange(url, HttpMethod.DELETE,
                new HttpEntity<>(msoPayload, buildRequestHeader()), CreateServiceInstanceResponse.class);

            logResponsePost(url, response);
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity<>(e.getHttpStatus());
        } catch (ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<CreateE2EServiceInstanceResponse> callE2EDeleteServiceInstance(String globalSubscriberId, String serviceType,
            String serviceInstanceId) {

            String url = soHostname + OnapComponentsUrlPaths.MSO_DELETE_E2ESERVICE_INSTANCE_PATH + serviceInstanceId;
            MSODeleteE2EPayload msoDeleteE2EPayload = new MSODeleteE2EPayload();
            msoDeleteE2EPayload.setGlobalSubscriberId(globalSubscriberId);
            msoDeleteE2EPayload.setServiceType(serviceType);
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calling SO DeleteE2EServiceInstance with url : " + url + " MSODeleteE2EPayload : " + msoDeleteE2EPayload.toString() );
            }
            
            try {
                ResponseEntity<DeleteE2EServiceInstanceResponse> deleteresponse = restTemplate.exchange(url, HttpMethod.DELETE,
                        new HttpEntity<>(msoDeleteE2EPayload, buildRequestHeader()), DeleteE2EServiceInstanceResponse.class);

                // For E2E Services , Create and Delete Service responses are different, to maintain consistentcy with ServiceInstances
                // Copy contents of DeleteE2EServiceInstanceResponse to CreateE2EServiceInstanceResponse
                CreateE2EServiceInstanceResponse dummyresponse = new CreateE2EServiceInstanceResponse();
                ServiceResponse serviceResponse = new ServiceResponse();
                dummyresponse.setService(serviceResponse);
                dummyresponse.getService().setOperationId(deleteresponse.getBody().getOperationId());
                dummyresponse.getService().setServiceId(serviceInstanceId);
                
                ResponseEntity<CreateE2EServiceInstanceResponse> response = new ResponseEntity(dummyresponse, deleteresponse.getStatusCode());  
                logE2EResponsePost(url, response);
                return response;

            } catch (BackendFunctionalException e) {
                LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
                return new ResponseEntity<>(e.getHttpStatus());
            } catch (ResourceAccessException e) {
                LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    	}

    private void logResponsePost(String url, ResponseEntity<CreateServiceInstanceResponse> response) {
        LOGGER.info(RESPONSE_STATUS + response.getStatusCodeValue());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("response body : {}", response.getBody().toString());
        }

        if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.CREATED)) {
            LOGGER.warn("HTTP call SO on {} returns {} , {}", url, response.getStatusCodeValue(),
                response.getBody().toString());
        }
    }

    private void logE2EResponsePost(String url, ResponseEntity<CreateE2EServiceInstanceResponse> response) {
        LOGGER.info(RESPONSE_STATUS + response.getStatusCodeValue());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("response body : {}", response.getBody().toString());
        }

        if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.CREATED)) {
            LOGGER.warn("HTTP call SO on {} returns {} , {}", url, response.getStatusCodeValue(),
                response.getBody().toString());
        }
    }

    public GetRequestStatusResponse callGetRequestStatus(String requestId) {
        String url = soHostname + OnapComponentsUrlPaths.MSO_GET_REQUEST_STATUS_PATH + requestId;

        try {

            ResponseEntity<GetRequestStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(buildRequestHeader()), GetRequestStatusResponse.class);
            logResponseGet(url, response);
            if (null == response)
                return null;
            else
                return response.getBody();

        } catch (BackendFunctionalException | ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return null;
        }
    }

public GetE2ERequestStatusResponse callE2EGetRequestStatus(String operationId, String serviceId) {
    	
    	StringBuilder callURL =
                new StringBuilder().append(soHostname).append(OnapComponentsUrlPaths.MSO_GET_E2EREQUEST_STATUS_PATH);
        String callUrlFormated = callURL.toString().replace("$serviceId", serviceId);
        callUrlFormated = callUrlFormated.replace("$operationId", operationId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO callE2EGetRequestStatus with url : " + callUrlFormated );
        }
        
        try {

            ResponseEntity<GetE2ERequestStatusResponse> response = restTemplate.exchange(callUrlFormated, HttpMethod.GET,
                new HttpEntity<>(buildRequestHeader()), GetE2ERequestStatusResponse.class);
            logE2EResponseGet(callUrlFormated, response);
            if (null == response)
                return null;
            else
                return response.getBody();

        } catch (BackendFunctionalException|ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + callUrlFormated + " ," + e);
            return null;
        }
    }

    private void logResponseGet(String url, ResponseEntity<GetRequestStatusResponse> response) {
        if (response != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response body : {}", response.getBody().toString());
            }
            LOGGER.info("response status : {}", response.getStatusCodeValue());
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.warn("HTTP call SO on {} returns {} , {}", url, response.getStatusCodeValue(),
                    response.getBody().toString());
            }
        } else {
            LOGGER.info("no response calling url {}", url);
        }
    }
    
    private void logE2EResponseGet(String url, ResponseEntity<GetE2ERequestStatusResponse> response) {
        if(response!=null){
        	if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response body : {}", response.getBody().toString());
            }
        	LOGGER.info("response status : {}", response.getStatusCodeValue());
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.warn("HTTP call SO on {} returns {} , {}", url, response.getStatusCodeValue(),
                    response.getBody().toString());
            }
        } else {
            LOGGER.info("no response calling url {}",url);
        }
    }

    private HttpHeaders buildRequestHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, soHeaderAuthorization);
        httpHeaders.add(X_FROM_APP_ID, soApiId);
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;
    }

}
