/**
 *     Copyright (c) 2019 Amdocs
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
package org.onap.nbi.apis.hub;

import org.onap.nbi.apis.hub.service.NotificationAspect;
import org.onap.nbi.commons.ResourceManagement;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.onap.nbi.apis.hub.model.Event;



@RestController
@RequestMapping("/listener")
public class ListenerResource extends ResourceManagement {

    @Autowired
    NotificationAspect notificationAspect;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerResource.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void receiveNotification(@RequestBody Event event) {
        try {
            LOGGER.debug("Received notification from external NBI || Sending it to original listener");
            notificationAspect.forwardNotificationToOriginalListener(event);
        }catch(BackendFunctionalException ex) {
            LOGGER.error("Unable to send the recieved notification to original Listener");

        }
    }
}
