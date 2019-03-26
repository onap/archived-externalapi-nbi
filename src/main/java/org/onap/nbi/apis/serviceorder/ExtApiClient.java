/**
 *     Copyright (c) 2019 Amdocs
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExtApiClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("${external.nbi.url}")
    private String externalNbiUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtApiClient.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public ResponseEntity<Object> postServiceOrder(ServiceOrder serviceOrder, String targetURL) {
        try {


            String url = externalNbiUrl.replace("{targetUrl}", targetURL) + "/serviceOrder";
            LOGGER.debug("Sending create service order request to " + url);
            String serviceOrderAsBody = mapper.writeValueAsString(serviceOrder);
            ResponseEntity<Object> response = postRequest(url, serviceOrderAsBody, buildRequestHeaders());
            LOGGER.info("Received response from " + targetURL + "with status : " + response.getStatusCode());
            LOGGER.debug("Response Body: " + response.getBody());
            return response;
        }catch(JsonProcessingException ex) {
            LOGGER.error("error occurred while parsing subscription data to Json: " + ex);
            throw new TechnicalException("error occurred while parsing subscription data to Json:"+ HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders buildRequestHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;
    }

    public ResponseEntity<Object> getServiceOrder(String serviceOrderId, String targetURL) {

        String url = externalNbiUrl.replace("{targetUrl}", targetURL) + "/serviceOrder/" + serviceOrderId;
        LOGGER.debug("Sending get service order request to " + url);
        ResponseEntity<Object> response = getRequest(url, buildRequestHeaders());
        LOGGER.info("Received response from " + targetURL + "with status : " + response.getStatusCode());
        LOGGER.debug("Response Body: " + response.getBody());
        return response;
    }


    private ResponseEntity<Object> getRequest(String url, HttpHeaders httpHeaders) {
        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), Object.class);
            return response;
        }catch(BackendFunctionalException ex) {
            LOGGER.error("Error occurred while sending post request to " + url);
            return new ResponseEntity<>("Found Error: " + ex.getBodyResponse(), ex.getHttpStatus());
        }catch (ResourceAccessException ex) {
            LOGGER.error("Error occurred while sending post request to " + url);
            return new ResponseEntity<>("Unable to access the resource at " + url, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}

    private ResponseEntity<Object> postRequest(String url, String body, HttpHeaders httpHeaders) {
        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders), Object.class);
            return response;
        }catch(BackendFunctionalException ex) {
          LOGGER.error("Error occurred while sending post request to " + url);
          return new ResponseEntity<>("Found Error: " + ex.getBodyResponse(), ex.getHttpStatus());
        }catch (ResourceAccessException ex) {
            LOGGER.error("Error occurred while sending post request to " + url);
            return new ResponseEntity<>("Unable to access the resource at " + url, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
