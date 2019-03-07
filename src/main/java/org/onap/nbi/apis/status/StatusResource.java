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
package org.onap.nbi.apis.status;

import org.onap.nbi.apis.status.model.ApplicationStatus;
import org.onap.nbi.apis.status.model.OnapModuleType;
import org.onap.nbi.apis.status.model.StatusType;
import org.onap.nbi.commons.JsonRepresentation;
import org.onap.nbi.commons.ResourceManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/status")
public class StatusResource extends ResourceManagement {

    @Autowired
    private StatusService statusService;

    @Value("${nbi.version}")
    private String version;

    private JsonRepresentation fullRepresentation = new JsonRepresentation().add("name").add("status").add("version")
        .add("components.name").add("components.status");

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> status(HttpServletRequest request, @RequestParam MultiValueMap<String, String> params) {

        ResponseEntity<Object> responseEntity = null;
        final String[] splitPath = request.getRequestURI().split("/");

        final String applicationName = splitPath[1];
        boolean fullStatus = Boolean.valueOf(params.getFirst("fullStatus"));
        final ApplicationStatus applicationStatus = buildNbiStatus(applicationName,fullStatus);

        // filter object
        Object response = this.getEntity(applicationStatus, fullRepresentation);

        responseEntity = ResponseEntity.ok(response);

        return responseEntity;
    }

    private ApplicationStatus buildNbiStatus(String applicationName, boolean fullStatus) {
        final ApplicationStatus applicationStatus = this.statusService.get(applicationName, version);

        if(fullStatus) {
            final ApplicationStatus sdcConnectivityStatus = this.statusService.getOnapConnectivity(OnapModuleType.SDC);
            final ApplicationStatus aaiConnectivityStatus = this.statusService.getOnapConnectivity(OnapModuleType.AAI);
            final ApplicationStatus soConnectivityStatus = this.statusService.getOnapConnectivity(OnapModuleType.SO);
            final ApplicationStatus dmaapConnectivityStatus = this.statusService.getOnapConnectivity(OnapModuleType.DMAAP);
            applicationStatus.addComponent(sdcConnectivityStatus).addComponent(aaiConnectivityStatus)
                .addComponent(soConnectivityStatus).addComponent(dmaapConnectivityStatus);
        }

        return applicationStatus;
    }

}
