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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
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
import org.springframework.http.MediaType;
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

    private String sdcGetUrl;
    private String sdcFindUrl;
    private String sdcHealthCheck;

    @PostConstruct
    private void setUpAndLogSDCUrl() {
        sdcGetUrl = new StringBuilder().append(sdcHost)
                .append(OnapComponentsUrlPaths.SDC_ROOT_URL + "/{id}" + OnapComponentsUrlPaths.SDC_GET_PATH).toString();
        sdcFindUrl = new StringBuilder().append(sdcHost).append(OnapComponentsUrlPaths.SDC_ROOT_URL).toString();
        sdcHealthCheck = new StringBuilder().append(sdcHost).append(OnapComponentsUrlPaths.SDC_HEALTH_CHECK).toString();

        LOGGER.info("SDC GET url :  " + sdcGetUrl);
        LOGGER.info("SDC FIND url :  " + sdcFindUrl);
        LOGGER.info("SDC HealthCheck :  " + sdcHealthCheck);

    }

    public Map callGet(String id) {

        String callUrl = sdcGetUrl.replace("{id}", id);
        UriComponentsBuilder callURLFormated = UriComponentsBuilder.fromHttpUrl(callUrl);
        ResponseEntity<Object> response = callSdc(callURLFormated.build().encode().toUri());
        return (LinkedHashMap) response.getBody();

    }

    public List<LinkedHashMap> callFind(MultiValueMap<String, String> parametersMap) {

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(sdcFindUrl);
        if (parametersMap != null) {
            Map<String, String> stringStringMap = parametersMap.toSingleValueMap();
            for (Entry<String, String> entry : stringStringMap.entrySet()) {
                if (!entry.getKey().equals("fields")) {
                    callURI.queryParam(entry.getKey(), entry.getValue());
                }
            }
        }

        ResponseEntity<Object> response = callSdc(callURI.build().encode().toUri());
        return (List<LinkedHashMap>) response.getBody();

    }

    public LinkedHashMap callCheckConnectivity() {

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(sdcHealthCheck);
        ResponseEntity<Object> response = callSdc(callURI.build().encode().toUri());
        return (LinkedHashMap) response.getBody();

    }

    public File callGetWithAttachment(String toscaModelUrl) {
        StringBuilder urlBuilder = new StringBuilder().append(sdcHost).append(toscaModelUrl);

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(urlBuilder.toString());

        File directory = new File("temptoscafile");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = "temptoscafile/" + System.currentTimeMillis() + "tosca.csar";
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

    public Path getServiceToscaModel(String uuid) throws IOException {
        StringBuilder urlBuilder = new StringBuilder().append(sdcHost).append(OnapComponentsUrlPaths.SDC_ROOT_URL)
                .append("/").append(uuid).append(OnapComponentsUrlPaths.SDC_TOSCA_PATH);

        UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(urlBuilder.toString());

        InputStream inputStream = (InputStream) callSdc(callURI.build().encode().toUri()).getBody();

        return createTmpFile(inputStream);
    }

    private Path createTmpFile(InputStream csarInputStream) throws IOException {
        Path csarFile = Files.createTempFile("csar", ".zip");
        Files.copy(csarInputStream, csarFile, StandardCopyOption.REPLACE_EXISTING);

        LOGGER.debug("Tosca file was saved at: {} ", csarFile.toAbsolutePath());

        return csarFile;
    }

    private HttpEntity<String> buildRequestHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HEADER_ECOMP_INSTANCE_ID, ecompInstanceId);
        httpHeaders.add(HEADER_AUTHORIZATION, sdcHeaderAuthorization);
        return new HttpEntity<>("parameters", httpHeaders);
    }

    private ResponseEntity<Object> callSdc(URI callURI) {
        ResponseEntity<Object> response =
                restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), Object.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("response body : {} ", response.getBody().toString());
        }
        LOGGER.info("response status : {}", response.getStatusCodeValue());
        loggDebugIfResponseKo(callURI.toString(), response);
        return response;
    }

    private ResponseEntity<byte[]> callSdcWithAttachment(URI callURI) {
        try {
            ResponseEntity<byte[]> response =
                    restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), byte[].class);
            LOGGER.info("response status : " + response.getStatusCodeValue());
            if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.warn("HTTP call SDC on {} returns {} ", callURI.toString(), response.getStatusCodeValue());
            }
            return response;

        } catch (BackendFunctionalException e) {
            LOGGER.error("HTTP call SDC on {} error : {}", callURI.toString(), e);
            return null;
        }
    }

    private void loggDebugIfResponseKo(String callURI, ResponseEntity<Object> response) {
        if (LOGGER.isWarnEnabled() && !response.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.warn("HTTP call SDC on {} returns {} , {}", callURI, response.getStatusCodeValue(),
                    response.getBody().toString());
        }
    }
}
