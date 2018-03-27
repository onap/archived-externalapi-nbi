package org.onap.nbi.apis.status;

import org.onap.nbi.apis.status.model.ApplicationStatus;
import org.onap.nbi.apis.status.model.StatusType;
import org.onap.nbi.commons.JsonRepresentation;
import org.onap.nbi.commons.ResourceManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/status")
public class StatusResource extends ResourceManagement<ApplicationStatus> {

    @Autowired
    private StatusService statusService;

    private JsonRepresentation fullRepresentation = new JsonRepresentation().add("name").add("status").add("version")
            .add("components.name").add("components.status");

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> status(HttpServletRequest request) {

        ResponseEntity<Object> responseEntity = null;

        final String[] splitPath = request.getRequestURI().split("/");

        final String applicationName = splitPath[1];
        final String applicationVersion = splitPath[3];

        final ApplicationStatus applicationStatus = this.statusService.get(applicationName, applicationVersion);

        final boolean isServiceFullyFunctional =
                StatusType.OK.equals(applicationStatus.getStatus()) ? applicationStatus.getComponents().stream()
                        .allMatch(componentStatus -> StatusType.OK.equals(componentStatus.getStatus())) : false;

        // filter object
        Object response = this.getEntity(applicationStatus, fullRepresentation);

        if (isServiceFullyFunctional) {
            responseEntity = ResponseEntity.ok(response);
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        return responseEntity;
    }

}
