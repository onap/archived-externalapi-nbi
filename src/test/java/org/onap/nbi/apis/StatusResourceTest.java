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
package org.onap.nbi.apis;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.status.StatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatusResourceTest {

    @Autowired
    StatusResource statusResource;

    @Value("${server.contextPath}")
    String contextPath;

    @Value("${nbi.version}")
    String version;

    private MockHttpServletRequest request;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        request.setRequestURI(contextPath);
    }

    @Test
    public void testHealthCheck() {
        ResponseEntity<Object> response = statusResource.status(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ObjectNode status = (ObjectNode) response.getBody();
        assertThat(status.get("name").textValue()).isEqualTo("nbi");
        assertThat(status.get("status").toString()).isEqualTo("OK");
        assertThat(status.get("version").textValue()).isEqualTo(version);
    }
}
