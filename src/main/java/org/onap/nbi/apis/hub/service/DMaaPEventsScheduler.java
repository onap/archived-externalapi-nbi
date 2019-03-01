/**
 * Copyright (c) 2019 Huawei
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.nbi.apis.hub.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("default")
@Service
@EnableScheduling
public class DMaaPEventsScheduler {

  @Autowired
  CheckDMaaPEventsManager checkDMaaPEventsManager;

  @Scheduled(fixedDelayString = "${dmaapCheck.schedule}",
      initialDelayString = "${dmaapCheck.initial}")
  private void processDMaaPEvents() {
      checkDMaaPEventsManager.checkForDMaaPAAIEvents();
    
  }
}

