package org.onap.nbi.apis.status;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatusTest {

    @Autowired
    StatusResource statusResource;

    private MockHttpServletRequest request;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        request.setRequestURI("/nbi/api/v1/status");
    }

    @Test
    public void testHealthCheck() {
        ResponseEntity<Object> response = statusResource.status(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ObjectNode status = (ObjectNode) response.getBody();
        assertThat(status.get("name").textValue()).isEqualTo("nbi");
        assertThat(status.get("status").toString()).isEqualTo("OK");
        assertThat(status.get("version").textValue()).isEqualTo("v1");
    }
}
