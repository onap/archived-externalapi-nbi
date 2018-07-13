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
package org.onap.nbi.apis.hub.service;

import org.onap.nbi.apis.hub.model.Event;
import org.onap.nbi.apis.hub.model.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Service
public class NotifierService {
    private final Logger logger = LoggerFactory.getLogger(NotifierService.class);

    @Async
    public void run(Subscriber subscriber, @Valid Event event) {
        ResponseEntity<String> re = new RestTemplate().postForEntity(subscriber.getCallback(), event, String.class);
        if (re.getStatusCode() == HttpStatus.OK) logger.debug("FAILED");
    }
}
