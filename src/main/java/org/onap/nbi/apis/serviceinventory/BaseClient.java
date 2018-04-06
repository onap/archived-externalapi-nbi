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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClient.class);

    @Autowired
    private RestTemplate restTemplate;

    protected ResponseEntity<Object> callApiGet(String callURL, HttpHeaders httpHeaders) {

        ResponseEntity<Object> response = restTemplate.exchange(callURL, HttpMethod.GET,
                new HttpEntity<>("parameters", httpHeaders), Object.class);
        LOGGER.debug("response body : " + response.getBody().toString());
        LOGGER.info("response status : " + response.getStatusCodeValue());
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.warn("HTTP call on " + callURL + " returns " + response.getStatusCodeValue() + ", "
                    + response.getBody().toString());
        }
        return response;
    }

}
