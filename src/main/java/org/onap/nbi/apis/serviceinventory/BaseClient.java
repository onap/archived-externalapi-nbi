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
