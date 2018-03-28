package org.onap.nbi.apis.resources;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.servicecatalog.ServiceSpecificationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.github.tomakehurst.wiremock.WireMockServer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiTest {

    @LocalServerPort
    int randomServerPort;

    String realServerPort;


    static public WireMockServer wireMockServer = new WireMockServer(8091);

    @Autowired
    ServiceSpecificationResource serviceSpecificationResource;

    @BeforeClass
    public static void setUp() throws Exception {
        wireMockServer.start();
    }

    @AfterClass
    public static void tearsDown() throws Exception {
        wireMockServer.stop();

    }

    @After
    public void tearsDownUpPort() throws Exception {
        wireMockServer.resetToDefaultMappings();
    }

    @Test
    @Ignore
    public void testServiceResourceGetCatalog() throws Exception {

        ResponseEntity<Object> resource =
                serviceSpecificationResource.getServiceSpecification("1e3feeb0-8e36-46c6-862c-236d9c626439", null);
        ServiceCatalogAssertions.assertGetServiceCatalog(resource);

    }

    @Test
    public void testServiceCatalogGetResourceWithoutTosca() throws Exception {

        ResponseEntity<Object> resource = serviceSpecificationResource
                .getServiceSpecification("1e3feeb0-8e36-46c6-862c-236d9c626439_withoutTosca", null);
        ServiceCatalogAssertions.asserGetServiceCatalogWithoutTosca(resource);

    }

    @Test
    public void testServiceCatalogFind() throws Exception {

        ResponseEntity<Object> resource = serviceSpecificationResource.findServiceSpecification(null);
        ServiceCatalogAssertions.assertFindServiceCatalog(resource);

    }


    @Test
    public void testServiceCatalogFindWithFilter() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "name");
        ResponseEntity<Object> resource = serviceSpecificationResource.findServiceSpecification(params);
        ServiceCatalogAssertions.assertFindServiceCatalogWIthFilter(resource);

    }

}
