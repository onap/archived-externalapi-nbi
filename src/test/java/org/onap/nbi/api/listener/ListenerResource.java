/**
 * Copyright (c) 2019 Orange
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
package org.onap.nbi.api.listener;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.onap.nbi.commons.ResourceManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/test/listener")
public class ListenerResource extends ResourceManagement {

    Logger logger = LoggerFactory.getLogger(ListenerResource.class);

    static Map<String, JsonNode> events = new ConcurrentHashMap<>();

    /*
        listener resource test for hub resource
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> postListener(@RequestBody JsonNode event) {
        if (logger.isDebugEnabled()) {
            logger.debug("POST event from nbi : {}", event.toString());
        }
        String eventId = event.get("eventId").asText();
        events.put(eventId, event);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(eventId)
            .toUri();

        return ResponseEntity.created(location).body(event);
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<JsonNode>> findEvents(@RequestParam MultiValueMap<String, String> params) {
        Collection<JsonNode> values = new ArrayList<>();
        String serviceOrderId = params.getFirst("serviceOrderId");
        if(StringUtils.isNotEmpty(serviceOrderId)) {
            for (JsonNode jsonNode : events.values()) {
                String id = jsonNode.get("event").get("id").asText();
                logger.info("found event with service order id : "+id);
                if(id.equals(serviceOrderId)) {
                    values.add(jsonNode);
                }
            }
            if(!values.isEmpty()) {
                return ResponseEntity.ok(values);
            } else {
                logger.error("cannot found events with service order id : "+serviceOrderId);
                return ResponseEntity.notFound().build();
            }
        } else {
            values=events.values();
        }
        return ResponseEntity.ok(values);
    }

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEvent(@PathVariable String eventId) {

        return ResponseEntity.ok(events.get(eventId));

    }

    @DeleteMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteEvent(@PathVariable String eventId) {

        events.remove(eventId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteEvents() {

        events.clear();
        return ResponseEntity.noContent().build();

    }

}
