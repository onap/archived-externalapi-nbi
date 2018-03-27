package org.onap.nbi.commons;

import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ResourceManagement<T extends Resource> {

    /**
     * Build default 201 filtered response for resource
     *
     * @param resource
     * @param jsonRepresentation
     * @return
     */
    protected ResponseEntity<Object> createResponse(final Resource resource,
            final JsonRepresentation jsonRepresentation) {

        URI location = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resource.getId())
                    .toUri();
        } else {
            location = URI.create("/");
        }


        // Get entity representation
        final Object entity = this.getEntity(resource, jsonRepresentation);

        return ResponseEntity.created(location).body(entity);

    }

    /**
     * Build default 200 filtered response for resource
     *
     * @param resource
     * @param jsonRepresentation
     * @return
     */
    protected ResponseEntity<Object> getResponse(final Object resource, final JsonRepresentation jsonRepresentation) {

        // Get entity representation
        final Object entity = this.getEntity(resource, jsonRepresentation);

        return ResponseEntity.ok(entity);

    }



    /**
     * Build default 206 filtered partial response for resource
     *
     * @param resource
     * @param jsonRepresentation
     * @return
     */
    protected ResponseEntity<Object> getPartialResponse(final Object resource,
            final JsonRepresentation jsonRepresentation) {

        // Get entity representation
        final Object entity = this.getEntity(resource, jsonRepresentation);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(entity);

    }


    /**
     * Build default 200 filtered response for resource collection
     *
     * @param resources
     * @param jsonRepresentation
     * @param headers
     * @return
     */
    protected ResponseEntity<Object> findResponse(final List<?> resources, final JsonRepresentation jsonRepresentation,
            HttpHeaders headers) {

        // Get entities representation
        final Object entities = this.getEntities(resources, jsonRepresentation);

        return ResponseEntity.ok().headers(headers).body(entities);

    }


    /**
     * Build 204 Empty response
     *
     * @return
     */
    protected ResponseEntity<Object> deleteResponse() {

        return ResponseEntity.noContent().build();
    }

    /**
     * Get entity, as resource or jacksonNode depending fields value
     *
     * @param resource
     * @param jsonRepresentation
     * @return
     */
    protected Object getEntity(final Object resource, JsonRepresentation jsonRepresentation) {

        Object entity;

        Set<String> attributes = jsonRepresentation.getAttributes();

        if (attributes == null || attributes.isEmpty() || attributes.contains(ReservedKeys.ALL_FIELDS)) {
            entity = resource;
        } else {
            entity = JacksonFilter.createNode(resource, jsonRepresentation);
        }

        return entity;
    }

    /**
     * Get entities, as resource list or jacksonNode depending fields value
     *
     * @param resources
     * @param jsonRepresentation
     * @return
     */
    protected Object getEntities(final List<?> resources, JsonRepresentation jsonRepresentation) {

        Object entities;

        Set<String> attributes = jsonRepresentation.getAttributes();

        if (attributes == null || attributes.isEmpty() || attributes.contains(ReservedKeys.ALL_FIELDS)) {
            entities = resources;
        } else {
            entities = JacksonFilter.createNodes(resources, jsonRepresentation);
        }

        return entities;
    }

}
