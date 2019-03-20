/**
 * Copyright (c) 2019 Vodafone Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.nbi.apis.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.onap.nbi.apis.hub.service.NotifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.onap.nbi.apis.hub.model.Event;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListenerResourceTarget {
    private static final ObjectMapper mapper = new ObjectMapper(new MappingJsonFactory());

    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private NotifierService notifier;

    Logger logger = LoggerFactory.getLogger(ListenerResourceTarget.class);

    static Map<String, JsonNode> events = new ConcurrentHashMap<>();

    /*
        listener resource test for hub resource
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postListenerResource(@RequestBody JsonNode event) {
        if (logger.isDebugEnabled()) {
            logger.debug("POST event from nbi : {}", event.toString());

        }
        try {
            Event eventListener = mapper.treeToValue(event, Event.class);
            subscriberRepository
                    .findSubscribersUsingEvent(eventListener)
                    .forEach(sub -> notifier.run(sub, eventListener));
        }
        catch(Exception e){
            logger.error("listener not called" + " ," + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}

