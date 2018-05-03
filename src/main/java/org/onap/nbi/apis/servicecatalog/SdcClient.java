/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onap.nbi.apis.servicecatalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.onap.nbi.OnapComponentsUrlPaths;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author user
 *
 */
@Service
public class SdcClient {

    public static final String HTTP_CALL_SDC_ON = "HTTP call SDC on ";
    @Autowired
    private RestTemplate restTemplate;

    @Value("${sdc.host}")
    private String sdcHost;

    @Value("${sdc.header.ecompInstanceId}")
    private String ecompInstanceId;

    @Value("${sdc.header.authorization}")
    private String sdcHeaderAuthorization;

    private static final String HEADER_ECOMP_INSTANCE_ID = "x-ecomp-instanceid";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(SdcClient.class);


    public LinkedHashMap callGet(String id) {
        StringBuilder urlBuilder = new StringBuilder().append(sdcHost).append(OnapComponentsUrlPaths.SDC_ROOT_URL)
                .append("/").append(id).append(OnapComponentsUrlPaths.SDC_GET_PATH);

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(urlBuilder.toString());

        ResponseEntity<Object> response = callSdc(callURI.build().encode().toUri());
        return (LinkedHashMap) response.getBody();

    }

    public List<LinkedHashMap> callFind(MultiValueMap<String, String> parametersMap) {

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(sdcHost + OnapComponentsUrlPaths.SDC_ROOT_URL);
        if (parametersMap != null) {
            Map<String, String> stringStringMap = parametersMap.toSingleValueMap();
            for (String key : stringStringMap.keySet()) {
                if (!key.equals("fields")) {
                    callURI.queryParam(key, stringStringMap.get(key));
                }
            }
        }

        ResponseEntity<Object> response = callSdc(callURI.build().encode().toUri());
        return (List<LinkedHashMap>) response.getBody();

    }


    public File callGetWithAttachment(String toscaModelUrl) {
        StringBuilder urlBuilder = new StringBuilder().append(sdcHost).append(toscaModelUrl);

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(urlBuilder.toString());


        String fileName = System.currentTimeMillis() + "tosca.csar";
        ResponseEntity<byte[]> response = callSdcWithAttachment(callURI.build().encode().toUri());
        File toscaFile = new File(fileName);
        try {
            FileOutputStream toscaFileStream = new FileOutputStream(toscaFile);
            if (response != null) {
                IOUtils.write(response.getBody(), toscaFileStream);
            }
            toscaFileStream.close();
        } catch (IOException e) {
            LOGGER.error("cannot get TOSCA File for url " + toscaModelUrl, e);
        }
        return toscaFile;

    }

    private HttpEntity<String> buildRequestHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_ECOMP_INSTANCE_ID, ecompInstanceId);
        httpHeaders.add(HEADER_AUTHORIZATION, sdcHeaderAuthorization);
        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        return entity;
    }


    private ResponseEntity<Object> callSdc(URI callURI) {
        ResponseEntity<Object> response =
                restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), Object.class);
        LOGGER.debug("response body : " + response.getBody().toString());
        LOGGER.info("response status : " + response.getStatusCodeValue());
        loggDebugIfResponseKo(callURI.toString(), response);
        return response;
    }


    private ResponseEntity<byte[]> callSdcWithAttachment(URI callURI) {
        try {
            ResponseEntity<byte[]> response =
                    restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), byte[].class);
            LOGGER.info("response status : " + response.getStatusCodeValue());
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.error(HTTP_CALL_SDC_ON + callURI.toString() + " returns " + response.getStatusCodeValue() + ", "
                        + response.getBody().toString());
            }
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error(HTTP_CALL_SDC_ON + callURI.toString() + " error " + e);
            return null;
        }
    }


    private void loggDebugIfResponseKo(String callURI, ResponseEntity<Object> response) {
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.warn(HTTP_CALL_SDC_ON + callURI + " returns " + response.getStatusCodeValue() + ", "
                + response.getBody().toString());
        }
    }
}


