package org.onap.nbi.apis.servicecatalog;

import java.util.LinkedHashMap;
import java.util.List;
import org.onap.nbi.commons.JsonRepresentation;
import org.onap.nbi.commons.Resource;
import org.onap.nbi.commons.ResourceManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/serviceSpecification")
public class ServiceSpecificationResource extends ResourceManagement<Resource> {


    @Autowired
    ServiceSpecificationService serviceSpecificationService;

    @GetMapping(value = "/{serviceSpecId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getServiceSpecification(@PathVariable String serviceSpecId,
            @RequestParam MultiValueMap<String, String> params) {
        LinkedHashMap response = serviceSpecificationService.get(serviceSpecId);
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

}
