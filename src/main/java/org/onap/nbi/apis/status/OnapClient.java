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
package org.onap.nbi.apis.status;

import java.text.MessageFormat;
import org.onap.nbi.apis.hub.service.CheckDMaaPEventsManager;
import org.onap.nbi.apis.servicecatalog.SdcClient;
import org.onap.nbi.apis.serviceinventory.AaiClient;
import org.onap.nbi.apis.serviceorder.SoClient;
import org.onap.nbi.apis.status.model.ApplicationStatus;
import org.onap.nbi.apis.status.model.OnapModuleType;
import org.onap.nbi.apis.status.model.StatusType;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
public class OnapClient {

    @Autowired
    private SdcClient sdcClient;

    @Autowired
    private AaiClient aaiClient;

    @Autowired
    private SoClient soClient;

    @Autowired
    private CheckDMaaPEventsManager checkDMaaPEventsManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(OnapClient.class);


    public ApplicationStatus checkConnectivity(OnapModuleType onapModuleType) {
        try {

            switch (onapModuleType) {
                case SDC:
                    sdcClient.callCheckConnectivity();
                    break;
                case AAI:
                    aaiClient.callCheckConnectivity();
                    break;
                case SO:
                    soClient.callCheckConnectivity();
                    break;
                case DMAAP:
                    checkDMaaPEventsManager.callDMaaPGetEvents();
                    break;
            }
        } catch (BackendFunctionalException e) {
            String message = MessageFormat
                .format("backend exception for {0}, status code {1}, body response {2}", onapModuleType,
                    e.getHttpStatus(), e.getBodyResponse());
            LOGGER.error(message);
            return new ApplicationStatus(onapModuleType.getValue() + " connectivity", StatusType.KO, null);
        } catch (ResourceAccessException e) {
            String message = MessageFormat
                .format("resource access exception for {0}, response {1}", onapModuleType, e.getMessage());
            LOGGER.error(message);
            return new ApplicationStatus(onapModuleType.getValue() + " connectivity", StatusType.KO, null);
        }
        return new ApplicationStatus(onapModuleType.getValue() + " connectivity", StatusType.OK, null);
    }


}