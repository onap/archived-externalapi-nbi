package org.onap.nbi.apis.serviceinventory;

import java.util.LinkedHashMap;
import java.util.List;
import org.onap.nbi.commons.JsonRepresentation;
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
@RequestMapping("/service")
public class ServiceInventoryResource extends ResourceManagement {

    @Autowired
    ServiceInventoryService serviceInventoryService;

    @GetMapping(value = "/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getServiceInventory(@PathVariable String serviceId,
            @RequestParam MultiValueMap<String, String> params) {

        LinkedHashMap response = serviceInventoryService.get(serviceId, params);

        JsonRepresentation filter = new JsonRepresentation(params);
        return this.getResponse(response, filter);

    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findServiceInventory(@RequestParam MultiValueMap<String, String> params) {

        List<LinkedHashMap> response = serviceInventoryService.find(params);
        JsonRepresentation filter = new JsonRepresentation(params);
        return this.findResponse(response, filter, null);

    }


}
