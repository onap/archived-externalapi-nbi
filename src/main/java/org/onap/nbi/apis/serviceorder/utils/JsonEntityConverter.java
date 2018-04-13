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
package org.onap.nbi.apis.serviceorder.utils;

import java.io.IOException;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonEntityConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String convertServiceOrderInfoToJson(ServiceOrderInfo serviceOrderInfo) {
        return MAPPER.valueToTree(serviceOrderInfo).toString();
    }

    public static ServiceOrderInfo convertJsonToServiceOrderInfo(String serviceOrderInfoJson) throws IOException {
        return MAPPER.treeToValue(MAPPER.readTree(serviceOrderInfoJson), ServiceOrderInfo.class);
    }
}
