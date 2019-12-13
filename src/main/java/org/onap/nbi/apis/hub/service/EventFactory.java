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

package org.onap.nbi.apis.hub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.onap.nbi.apis.hub.model.Event;
import org.onap.nbi.apis.hub.model.EventType;
import org.onap.nbi.apis.hub.model.ServiceInstanceEvent;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.commons.JacksonFilter;
import org.onap.nbi.commons.JsonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventFactory {

    private static final ObjectMapper mapper = new ObjectMapper(new MappingJsonFactory());
    private static final Logger logger = LoggerFactory.getLogger(EventFactory.class);

    public static Event getEvent(EventType eventType, ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem) {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventDate(new Date());
        event.setEventType(eventType.value());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mapper.setDateFormat(df);

        JsonNode serviceOrderJson = mapper.valueToTree(filterServiceOrder(serviceOrder));

        if (EventType.SERVICE_ORDER_ITEM_STATE_CHANGE.equals(eventType)) {
            JsonNode serviceOrderItemJson = mapper.valueToTree(serviceOrderItem);
            ((ObjectNode) serviceOrderJson).putArray("orderItem").add(serviceOrderItemJson);
        }

        event.setEvent(serviceOrderJson);

        return event;
    }

    public static Event getEvent(EventType eventType, ServiceInstanceEvent serviceInstanceEvent) {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventDate(new Date());
        event.setEventType(eventType.value());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mapper.setDateFormat(df);

        JsonNode serviceInstanceJson = mapper.valueToTree(serviceInstanceEvent);

        event.setEvent(serviceInstanceJson);

        return event;
    }

    public static Event getEvent(EventType eventType, String eventString) {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventDate(new Date());
        event.setEventType(eventType.value());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mapper.setDateFormat(df);
        JsonNode serviceInstanceJson = null;
        try {
            serviceInstanceJson = mapper.readTree(eventString);
            event.setEvent(serviceInstanceJson);
            return event;
        } catch (IOException e) {
            logger.error("IO Error when parsing Event JSON String {} ", eventString, e.getMessage());
        }
        return null;
    }

    /**
     * Filter ServiceOrderObject to produce a lightweight object that fit the eventBody specification
     *
     * @param serviceOrder
     * @return
     */
    private static Object filterServiceOrder(final ServiceOrder serviceOrder) {

        Object filteredServiceOrder = null;

        if (serviceOrder != null) {
            JsonRepresentation jsonRepresentation = new JsonRepresentation();
            jsonRepresentation.add("id").add("href").add("externalId").add("state").add("orderDate")
                    .add("completionDateTime").add("orderItem");

            filteredServiceOrder = JacksonFilter.createNode(serviceOrder, jsonRepresentation);
        }

        return filteredServiceOrder;
    }
}
