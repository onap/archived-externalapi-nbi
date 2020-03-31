/**
 * Copyright (c) 2018 Huawei
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

package org.onap.nbi.apis.serviceorder.utils;

import java.util.Map;

import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;

public class E2EServiceUtils {

    public static boolean isE2EService(ServiceOrderItemInfo serviceOrderItemInfo) {
        Map<String, Object> sdcInfos = serviceOrderItemInfo.getCatalogResponse();
        boolean e2eService = false;
        if (sdcInfos.get("category") != null) {
            String category = ((String) sdcInfos.get("category")).toLowerCase();
            // Until SO comes up with one consolidated API for Service CRUD, ExtAPI has to be handle SO (serviceInstance
            // and e2eServiceInstances )APIs for service CRUD
            // All E2E Services are required to be created in SDC under category "E2E Services" until SO fixes the
            // multiple API issue.
            if (category.startsWith("e2e") || category.startsWith("cst")) {
                e2eService = true;
            }
        }
        return e2eService;
    }

}
