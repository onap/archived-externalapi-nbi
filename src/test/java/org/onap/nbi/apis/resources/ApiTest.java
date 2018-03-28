package org.onap.nbi.apis.resources;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.servicecatalog.ServiceSpecificationResource;
import org.onap.nbi.apis.serviceinventory.ServiceInventoryResource;
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

    @Autowired
    ServiceInventoryResource serviceInventoryResource;

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

    // serviceCatalog

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

    // serviceInventory

    @Test
    public void testServiceResourceGetInventory() throws Exception {

        String serviceName = "vFW";
        String serviceId = "e4688e5f-61a0-4f8b-ae02-a2fbde623bcb";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.name", serviceName);
        params.add("relatedParty.id", "6490");
        ResponseEntity<Object> resource = serviceInventoryResource.getServiceInventory(serviceId, params);
        ServiceInventoryAssertions.assertServiceInventoryGet(resource);

    }


    @Test
    public void testServiceResourceGetInventoryWithServiceSpecId() throws Exception {

        String serviceId = "e4688e5f-61a0-4f8b-ae02-a2fbde623bcb";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.id", "1e3feeb0-8e36-46c6-862c-236d9c626439");
        params.add("relatedParty.id", "6490");
        ResponseEntity<Object> resource = serviceInventoryResource.getServiceInventory(serviceId, params);
        ServiceInventoryAssertions.assertServiceInventoryGet(resource);

    }


    @Test
    public void testServiceInventoryFind() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String serviceName = "vFW";
        params.add("serviceSpecification.name", serviceName);
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFind(resource);

    }


    @Test
    public void testServiceInventoryFindWithServiceSpecId() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceSpecification.id", "1e3feeb0-8e36-46c6-862c-236d9c626439");
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFind(resource);

    }


    @Test
    public void testServiceInventoryFindWithoutParameter() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("relatedParty.id", "6490");

        ResponseEntity<Object> resource = serviceInventoryResource.findServiceInventory(params);
        ServiceInventoryAssertions.assertServiceInventoryFindWithoutParameter(resource);

    }

}
