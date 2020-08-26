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

package org.onap.nbi.apis.servicecatalog;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.onap.nbi.OnapComponentsUrlPaths;
import org.onap.nbi.apis.servicecatalog.model.ServiceSpecificationRequest;
import org.onap.nbi.commons.JsonRepresentation;
import org.onap.nbi.commons.ResourceManagement;
import org.onap.nbi.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(OnapComponentsUrlPaths.SERVICE_SPECIFICATION_PATH)
public class ServiceSpecificationResource extends ResourceManagement {

    @Autowired
    ServiceSpecificationService serviceSpecificationService;

    @GetMapping(value = "/{serviceSpecId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getServiceSpecification(@PathVariable String serviceSpecId,
                                                          @RequestParam MultiValueMap<String, String> params) {
        Map response = serviceSpecificationService.get(serviceSpecId);

        if (response != null) {
            ArrayList<Map<String, Object>> resourseSpecificationMap= (ArrayList<Map<String, Object>>) response.get("resourceSpecification");
            for (Map<String, Object> map : resourseSpecificationMap) {
                map.remove("childResourceSpecification");
                map.remove("serviceInstanceParams");
                map.remove("InstanceSpecification");
            }
            response.put("resourceSpecification", resourseSpecificationMap);
        }

        JsonRepresentation filter = new JsonRepresentation(params);
        if (response.get("serviceSpecCharacteristic") != null) {
            return this.getResponse(response, filter);
        } else {
            return this.getPartialResponse(response, filter);

        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findServiceSpecification(@RequestParam MultiValueMap<String, String> params) {
        List<LinkedHashMap> response = serviceSpecificationService.find(params);
        JsonRepresentation filter = new JsonRepresentation(params);
        return this.findResponse(response, filter, null);
    }

    @GetMapping(value = "/{serviceSpecId}/specificationInputSchema", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findSpecificationInputSchema(@PathVariable String serviceSpecId,
                                                               @RequestParam MultiValueMap<String, String> params) {
        String response = serviceSpecificationService.getInputSchema(serviceSpecId);
        JsonRepresentation filter = new JsonRepresentation(params);
        return this.getResponse(response, filter);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createServiceSpecification(@RequestHeader(value = "USER_ID", required = true) String userId,
                                             @Valid @RequestBody ServiceSpecificationRequest serviceSpecificationRequest, BindingResult result) {
        if (null == userId || userId.isEmpty()) {
            result.addError(new ObjectError("USER_ID", "USER_ID is missing in header!"));
        }
        if (result.hasErrors()) {
            throw new ValidationException(result.getAllErrors());
        }
        Map serviceCatalogResponse = serviceSpecificationService.create(userId, serviceSpecificationRequest);

        return createResponse(serviceCatalogResponse);
    }
    /**
     *
     * @param resource
     * @return
     */
    private ResponseEntity<Object> createResponse(final Map resource) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resource.get("id"))
                .toUri();
        return ResponseEntity.created(location).body(resource);

    }

}