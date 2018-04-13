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
package org.onap.nbi.apis.assertions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import static org.assertj.core.api.Assertions.assertThat;

public class ServiceInventoryAssertions {



    public static void assertServiceInventoryGet(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        LinkedHashMap service = (LinkedHashMap) resource.getBody();
        assertThat(service.get("id")).isEqualTo("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
        assertThat(service.get("name")).isEqualTo("NewFreeRadius-service-instance-01");
        assertThat(service.get("hasStarted")).isEqualTo("yes");
        assertThat(service.get("type")).isEqualTo("service-instance");
        assertThat(service.get("@type")).isEqualTo("serviceONAP");
        LinkedHashMap relatedParty = (LinkedHashMap) service.get("relatedParty");
        assertThat(relatedParty.get("role")).isEqualTo("ONAPcustomer");
        assertThat(relatedParty.get("id")).isEqualTo("6490");
        LinkedHashMap serviceSpecification = (LinkedHashMap) service.get("serviceSpecification");
        assertThat(serviceSpecification.get("id")).isEqualTo("98d95267-5e0f-4531-abf8-f14b90031dc5");
        assertThat(serviceSpecification.get("invariantUUID")).isEqualTo("709d157b-52fb-4250-976e-7133dff5c347");
        assertThat(serviceSpecification.get("@type")).isEqualTo("ONAPservice");


        assertThat(((ArrayList) service.get("supportingResource")).size()).isEqualTo(2);
        LinkedHashMap resource1 = (LinkedHashMap) ((ArrayList) service.get("supportingResource")).get(0);
        assertThat(resource1.get("id")).isEqualTo("cb80fbb6-9aa7-4ac5-9541-e14f45de533e");
        assertThat(resource1.get("name")).isEqualTo("NewFreeRadius-VNF-instance-01");
        assertThat(resource1.get("status")).isEqualTo("PREPROV");
        assertThat(resource1.get("modelInvariantId")).isEqualTo("f5993703-977f-4346-a1c9-c1884f8cfd8d");
        assertThat(resource1.get("modelVersionId")).isEqualTo("902438f7-1e4c-492d-b7cc-8650e13b8aeb");
        assertThat(resource1.get("@referredType")).isEqualTo("ONAP resource");
    }


    public static void assertServiceInventoryFind(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArrayList body = (ArrayList) resource.getBody();
        assertThat(body.size()).isEqualTo(1);
        LinkedHashMap service1 = (LinkedHashMap) body.get(0);
        assertThat(service1.get("id")).isEqualTo("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
        assertThat(service1.get("name")).isEqualTo("NewFreeRadius-service-instance-01");
        LinkedHashMap relatedParty = (LinkedHashMap) service1.get("relatedParty");
        assertThat(relatedParty.get("role")).isEqualTo("ONAPcustomer");
        assertThat(relatedParty.get("id")).isEqualTo("6490");
        LinkedHashMap serviceSpecification = (LinkedHashMap) service1.get("serviceSpecification");
        assertThat(serviceSpecification.get("name")).isEqualTo("vFW");
        assertThat(serviceSpecification.get("id")).isEqualTo("98d95267-5e0f-4531-abf8-f14b90031dc5");
    }


    public static void assertServiceInventoryFindWithoutParameter(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArrayList body = (ArrayList) resource.getBody();
        assertThat(body.size()).isEqualTo(2);
        LinkedHashMap service1 = (LinkedHashMap) body.get(0);
        assertThat(service1.get("id")).isEqualTo("vfw-service-id");
        assertThat(service1.get("name")).isEqualTo("vfw-service-name");
        LinkedHashMap relatedParty = (LinkedHashMap) service1.get("relatedParty");
        assertThat(relatedParty.get("role")).isEqualTo("ONAPcustomer");
        assertThat(relatedParty.get("id")).isEqualTo("6490");
        LinkedHashMap serviceSpecification = (LinkedHashMap) service1.get("serviceSpecification");
        assertThat(serviceSpecification.get("name")).isEqualTo("vFW-service-2VF-based");
        assertThat(serviceSpecification.get("id")).isEqualTo("9vfw-service-modek-version-id");


        LinkedHashMap service2 = (LinkedHashMap) body.get(1);
        assertThat(service2.get("id")).isEqualTo("e4688e5f-61a0-4f8b-ae02-a2fbde623bcb");
        assertThat(service2.get("name")).isEqualTo("NewFreeRadius-service-instance-01");
        relatedParty = (LinkedHashMap) service1.get("relatedParty");
        assertThat(relatedParty.get("role")).isEqualTo("ONAPcustomer");
        assertThat(relatedParty.get("id")).isEqualTo("6490");
        serviceSpecification = (LinkedHashMap) service2.get("serviceSpecification");
        assertThat(serviceSpecification.get("name")).isEqualTo("vFW");
        assertThat(serviceSpecification.get("id")).isEqualTo("98d95267-5e0f-4531-abf8-f14b90031dc5");

    }

}
