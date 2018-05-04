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
package org.onap.nbi.apis.serviceinventory;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NbiClient extends BaseClient {

    @Value("${nbi.url}")
    private String nbiUrl;

    public Map getServiceSpecification(String id) {
        StringBuilder callURL = new StringBuilder().append(nbiUrl).append("/serviceSpecification/").append(id);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders());
        return (LinkedHashMap) response.getBody();
    }
}
