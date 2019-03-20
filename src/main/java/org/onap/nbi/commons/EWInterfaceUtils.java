/**
 * Copyright (c) 2019 Vodafone Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.nbi.commons;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


@Service
public class EWInterfaceUtils {

    public static final String RESPONSE_STATUS = "response status : ";
    public static final String RETURNS = " returns ";
    public static final String ERROR_ON_CALLING = "error on calling ";
    private static final Logger LOGGER = LoggerFactory.getLogger( EWInterfaceUtils.class);
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


    public ResponseEntity<Object> callPostRequestTarget(Object obj, String targetUrl) {

        try {
            ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.POST,
                    new HttpEntity<>(obj, buildRequestHeader()), Object.class);

            logResponseGet(targetUrl, response);
            if (null == response) {
                return null;
            } else {
                return response;
            }

        } catch (BackendFunctionalException | ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING  + " ," + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Object> callGetRequestTarget(String targetUrl) {
        try {
            ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    new HttpEntity<>(buildRequestHeader()), Object.class);
            LOGGER.info("response status : {}", targetUrl);
            logResponseGet(targetUrl, response);
            if (null == response) {
                return null;
            } else {
                return response;
            }

        } catch (BackendFunctionalException | ResourceAccessException e) {
            LOGGER.error(ERROR_ON_CALLING + targetUrl + " ," + e);
            return null;
        }
    }
    private void logResponseGet(String url, ResponseEntity<Object> response) {
        if (response != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response body : {}", response.getBody().toString());
            }
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals( HttpStatus.OK)) {
                LOGGER.warn("HTTP call EWInterface on {} returns {} , {}", url, response.getStatusCodeValue(),
                        response.getBody().toString());
            }
        } else {
            LOGGER.info("no response calling url {}", url);
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
