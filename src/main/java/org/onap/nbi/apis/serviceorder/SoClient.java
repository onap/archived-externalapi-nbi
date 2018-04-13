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
import org.onap.nbi.apis.serviceorder.model.consumer.CreateServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.GetRequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestDetails;
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
import org.springframework.web.client.RestTemplate;

@Service
public class SoClient {

    public static final String RESPONSE_STATUS = "response status : ";
    public static final String RESPONSE_BODY = "response body : ";
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


    public ResponseEntity<CreateServiceInstanceResponse> callCreateServiceInstance(RequestDetails requestDetails) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO CreateServiceInstance with requestDetails : " + requestDetails.toString());
        }

        String url = soHostname + OnapComponentsUrlPaths.MSO_CREATE_SERVICE_INSTANCE_PATH;

        HttpEntity<RequestDetails> requestDetailEntity = new HttpEntity<>(requestDetails, buildRequestHeader());

        try {
            ResponseEntity<CreateServiceInstanceResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(requestDetailEntity, buildRequestHeader()), CreateServiceInstanceResponse.class);

            logResponsePost(url, response);
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return null;
        }
    }

    public ResponseEntity<CreateServiceInstanceResponse> callDeleteServiceInstance(RequestDetails requestDetails,
            String serviceId) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling SO DeleteServiceInstance with requestDetails : " + requestDetails.toString());
        }

        String url = soHostname + OnapComponentsUrlPaths.MSO_DELETE_REQUEST_STATUS_PATH + serviceId;

        HttpEntity<RequestDetails> requestDetailEntity = new HttpEntity<>(requestDetails, buildRequestHeader());

        try {
            ResponseEntity<CreateServiceInstanceResponse> response = restTemplate.exchange(url, HttpMethod.DELETE,
                    new HttpEntity<>(requestDetailEntity, buildRequestHeader()), CreateServiceInstanceResponse.class);

            logResponsePost(url, response);
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return null;
        }

    }

    private void logResponsePost(String url, ResponseEntity<CreateServiceInstanceResponse> response) {
        LOGGER.info(RESPONSE_STATUS + response.getStatusCodeValue());
        LOGGER.debug(RESPONSE_BODY + response.getBody().toString());

        if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
            LOGGER.warn("HTTP call SO on " + url + RETURNS + response.getStatusCodeValue() + ", "
                    + response.getBody().toString());
        }
    }


    public GetRequestStatusResponse callGetRequestStatus(String requestId) {
        String url = soHostname + OnapComponentsUrlPaths.MSO_GET_REQUEST_STATUS_PATH + requestId;

        try {

            ResponseEntity<GetRequestStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(buildRequestHeader()), GetRequestStatusResponse.class);
            logResponseGet(url, response);
            return response.getBody();

        } catch (BackendFunctionalException e) {
            LOGGER.error(ERROR_ON_CALLING + url + " ," + e);
            return null;
        }
    }

    private void logResponseGet(String url, ResponseEntity<GetRequestStatusResponse> response) {
        LOGGER.debug(RESPONSE_BODY + response.getBody().toString());
        LOGGER.info(RESPONSE_STATUS + response.getStatusCodeValue());
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.warn("HTTP call on " + url + RETURNS + response.getStatusCodeValue() + ", "
                    + response.getBody().toString());
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
