/**
 * Copyright (c) 2018 Orange
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

package org.onap.nbi.apis.servicecatalog;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.serviceorder.model.consumer.VFModelInfo;
import org.onap.nbi.exceptions.TechnicalException;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class ToscaInfosProcessorTest {

    @Autowired
    ToscaInfosProcessor toscaInfosProcessor;
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

    @Test
    public void buildResponseWithSdcToscaParser() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile())
                .toPath().toAbsolutePath();
        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "7baa7742-3a13-4288-8330-868015adc340");
        resources.add(resource1);
        LinkedHashMap resource2 = new LinkedHashMap();
        resource2.put("id", "81b9430b-8abe-45d6-8bf9-f41a8f5c735f");
        resources.add(resource2);
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification", resources);

        try {
            toscaInfosProcessor.buildResponseWithSdcToscaParser(path, response);
        } catch (SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : "
                    + path.toString() + " " + e.getMessage());
        }
        resources = (List<LinkedHashMap>) response.get("resourceSpecification");
        List<LinkedHashMap> serviceSpecCharacteristic = new ArrayList<>();
        serviceSpecCharacteristic = (List<LinkedHashMap>) response.get("serviceSpecCharacteristic");
        assertThat(serviceSpecCharacteristic.get(0).get("name")).isEqualTo("sdwanconnectivity0_topology");
        assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
        assertThat(serviceSpecCharacteristic.get(0).get("required")).isEqualTo(true);
        assertThat(serviceSpecCharacteristic.get(1).get("name")).isEqualTo("sdwanconnectivity0_name");
        assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
        assertThat(serviceSpecCharacteristic.get(1).get("required")).isEqualTo(true);
        assertThat(resources.get(0).get("modelCustomizationId")).isEqualTo("94ec574b-2306-4cbd-8214-09662b040f73");
        assertThat(resources.get(1).get("modelCustomizationId")).isEqualTo("a7baba5d-6ac3-42b5-b47d-070841303ab1");

    }

    @Test
    public void buildResponseWithSdcToscaParserWithDefaultInputs() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-Sotnvpninfraservice-csar.csar").getFile())
                .toPath().toAbsolutePath();
        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "218df3c3-50dd-4c26-9e36-4771387bb771");
        resources.add(resource1);
        LinkedHashMap resource2 = new LinkedHashMap();
        resource2.put("id", "81b9430b-8abe-45d6-8bf9-f41a8f5c735f");
        resources.add(resource2);
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification", resources);

        try {
            toscaInfosProcessor.buildResponseWithSdcToscaParser(path, response);
        } catch (SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : "
                    + path.toString() + " " + e.getMessage());
        }
        resources = (List<LinkedHashMap>) response.get("resourceSpecification");
        List<LinkedHashMap> serviceSpecCharacteristic = new ArrayList<>();
        serviceSpecCharacteristic = (List<LinkedHashMap>) response.get("serviceSpecCharacteristic");
        assertThat(resources.get(0).get("modelCustomizationId")).isEqualTo("b44071c8-04fd-4d6b-b6af-772cbfaa1129");
        assertThat(resources.get(1).get("modelCustomizationId")).isEqualTo("c3612284-6c67-4d8c-8b41-b699cc90e76d");
        assertThat(serviceSpecCharacteristic.get(12).get("serviceSpecCharacteristicValue")).isNull();
        assertThat(serviceSpecCharacteristic.get(13).get("serviceSpecCharacteristicValue")).isNotNull();
    }

    @Test
    public void buildResponseWithSdcToscaParserwithMetaDataMisMatch() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile())
                .toPath().toAbsolutePath();
        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "some bad resource id no in TOSCA CSAR");
        resources.add(resource1);
        LinkedHashMap resource2 = new LinkedHashMap();
        resource2.put("id", "some bad resource id no in TOSCA CSAR");
        resources.add(resource2);
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification", resources);

        try {
            toscaInfosProcessor.buildResponseWithSdcToscaParser(path, response);
        } catch (SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : "
                    + path.toString() + " " + e.getMessage());
        }
        resources = (List<LinkedHashMap>) response.get("resourceSpecification");
        List<LinkedHashMap> serviceSpecCharacteristic = new ArrayList<>();
        serviceSpecCharacteristic = (List<LinkedHashMap>) response.get("serviceSpecCharacteristic");
        assertThat(serviceSpecCharacteristic.get(0).get("name")).isEqualTo("sdwanconnectivity0_topology");
        assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
        assertThat(serviceSpecCharacteristic.get(0).get("required")).isEqualTo(true);
        assertThat(serviceSpecCharacteristic.get(1).get("name")).isEqualTo("sdwanconnectivity0_name");
        assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
        assertThat(serviceSpecCharacteristic.get(1).get("required")).isEqualTo(true);
        // Check that resources cannot be found in the TOSCA template
        assertThat(resources.get(0).get("modelCustomizationId")).isNull();
        assertThat(resources.get(1).get("modelCustomizationId")).isNull();

    }

    @Test
    public void testBuildAndSaveResponseWithSdcToscaParser() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-Sotnvpninfraservice-csar.csar").getFile())
                .toPath().toAbsolutePath();

        LinkedHashMap response = new LinkedHashMap();
        response.put("version", "1.0");
        response.put("name", "Service_vMME");
        response.put("description", "some service characteristics schema");
        response.put("id", "7f5e5139-768d-4410-a871-c41430785214");

        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "7baa7742-3a13-4288-8330-868015adc340");
        resources.add(resource1);
        LinkedHashMap resource2 = new LinkedHashMap();
        resource2.put("id", "81b9430b-8abe-45d6-8bf9-f41a8f5c735f");
        resources.add(resource2);

        response.put("resourceSpecification", resources);

        LinkedHashMap serviceSpecCharacteristicValue = new LinkedHashMap();
        serviceSpecCharacteristicValue.put("valueType", "object");
        serviceSpecCharacteristicValue.put("@schemaLocation",
                "/serviceSpecification/7f5e5139-768d-4410-a871-c41430785214/specificationInputSchema");
        serviceSpecCharacteristicValue.put("@type", "Service_vMME_ServiceCharacteristic");

        LinkedHashMap serviceSpecCharacteristic = new LinkedHashMap();
        serviceSpecCharacteristic.put("name", "Service_vMME_ServiceCharacteristics");
        serviceSpecCharacteristic.put("description",
                "This object describes all the inputs needed from the client to interact with the Service_vMME Service Topology");
        // using object to match examples in specifications
        serviceSpecCharacteristic.put("valueType", "object");
        serviceSpecCharacteristic.put("@type", "ONAPServiceCharacteristic");
        serviceSpecCharacteristic.put("@schemaLocation", "null");
        serviceSpecCharacteristic.put("serviceSpecCharacteristicValue", serviceSpecCharacteristicValue);
        try {
            toscaInfosProcessor.buildAndSaveResponseWithSdcToscaParser(path, response);
        } catch (SdcToscaParserException ex) {
            throw new TechnicalException("unable to build response " + ex.getMessage());
        }
        assertThat(response.get("serviceSpecCharacteristic")).isEqualTo(serviceSpecCharacteristic);
    }

    @Test
    public void testBuildAndSaveResponseWithSdcToscaParserWithInputListType() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-MscmEvplService-csar.csar").getFile()).toPath()
                .toAbsolutePath();

        LinkedHashMap response = new LinkedHashMap();
        response.put("version", "1.0");
        response.put("name", "MSCM-EVPL-Service");
        response.put("description", "MSCM EVPL Service");
        response.put("id", "66a66cc3-178c-45fd-82c2-494336cb3665");

        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "f5f487df-8c02-4485-81d4-695c50e24b22");
        resources.add(resource1);
        LinkedHashMap resource2 = new LinkedHashMap();
        resource2.put("id", "65c34b35-e8ab-426a-b048-d707467f68b2");
        resources.add(resource2);

        response.put("resourceSpecification", resources);

        LinkedHashMap serviceSpecCharacteristicValue = new LinkedHashMap();
        serviceSpecCharacteristicValue.put("valueType", "object");
        serviceSpecCharacteristicValue.put("@schemaLocation",
                "/serviceSpecification/66a66cc3-178c-45fd-82c2-494336cb3665/specificationInputSchema");
        serviceSpecCharacteristicValue.put("@type", "MSCM-EVPL-Service_ServiceCharacteristic");

        LinkedHashMap serviceSpecCharacteristic = new LinkedHashMap();
        serviceSpecCharacteristic.put("name", "MSCM-EVPL-Service_ServiceCharacteristics");
        serviceSpecCharacteristic.put("description",
                "This object describes all the inputs needed from the client to interact with the MSCM-EVPL-Service Service Topology");
        // using object to match examples in specifications
        serviceSpecCharacteristic.put("valueType", "object");
        serviceSpecCharacteristic.put("@type", "ONAPServiceCharacteristic");
        serviceSpecCharacteristic.put("@schemaLocation", "null");
        serviceSpecCharacteristic.put("serviceSpecCharacteristicValue", serviceSpecCharacteristicValue);
        try {
            toscaInfosProcessor.buildAndSaveResponseWithSdcToscaParser(path, response);
        } catch (SdcToscaParserException ex) {
            throw new TechnicalException("unable to build response " + ex.getMessage());
        }
        assertThat(response.get("serviceSpecCharacteristic")).isEqualTo(serviceSpecCharacteristic);
    }

    
    @Test
    public void testBuildAndSaveResponseWithSdcToscaParserWithVFModule() {

	ClassLoader classLoader = getClass().getClassLoader();

	// Adding Path to TOSCA File to provide as parameter
	Path path = new File(classLoader.getResource("toscafile/service-VlbService-csar.csar").getFile()).toPath()
			.toAbsolutePath();

	// Preparing Response Data
	LinkedHashMap<String, Object> response = new LinkedHashMap<>();
	response.put("version", "1.0");
	response.put("name", "VLB_Service");
	response.put("description", "VLB_Service");
	response.put("id", "82c9fbb4-656c-4973-8c7f-172b22b5fa8f");

	// Resources
	List<LinkedHashMap<String, Object>> resources = new ArrayList<>();
	LinkedHashMap<String, Object> resource1 = new LinkedHashMap<>();
	resource1.put("id", "35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc");
	resources.add(resource1);

	// Resources to put in response as resourceSpecification
	response.put("resourceSpecification", resources);

	// Test Data for VFModule 1:: An object of vFModelInfo1
	VFModelInfo vFModelInfo1 = new VFModelInfo();
	vFModelInfo1.setModelName("VlbVsp..dnsscaling..module-1");
	vFModelInfo1.setModelUuid("9bfd197c-7e18-41bd-927d-57102a6fda7e");
	vFModelInfo1.setModelInvariantUuid("888b6342-8aea-4416-b485-e24726c1f964");
	vFModelInfo1.setModelVersion("1");
	vFModelInfo1.setModelCustomizationUuid("4c387136-2fa2-420f-94e9-3312f863a352");

	// Test Data for VFModule 2:: An object of vFModelInfo2
	VFModelInfo vFModelInfo2 = new VFModelInfo();
	vFModelInfo2.setModelName("VlbVsp..base_vlb..module-0");
	vFModelInfo2.setModelUuid("d0325d26-43f2-4c6f-aff5-2832ac2d8ab0");
	vFModelInfo2.setModelInvariantUuid("bcbdfc80-4fb1-4c3e-b4e3-77721bac61db");
	vFModelInfo2.setModelVersion("1");
	vFModelInfo2.setModelCustomizationUuid("0895caa9-b7d3-4e02-9a3c-8337c4076948");

	// Test data for list of vFModelInfo
	List<VFModelInfo> vfModelInfoListTestData = new ArrayList<>();
	vfModelInfoListTestData.add(vFModelInfo1);
	vfModelInfoListTestData.add(vFModelInfo2);

	// Calling buildAndSaveResponseWithSdcToscaParser with tosca file and prepared
	// response as parameter
	try {
		toscaInfosProcessor.buildAndSaveResponseWithSdcToscaParser(path, response);
	} catch (SdcToscaParserException e) {
		throw new TechnicalException("unable to build response from tosca csar using sdc-parser : "
				+ path.toString() + " " + e.getMessage());
	}

	// Getting resourceSpecifications from response
	List<LinkedHashMap> resourceSpecifications = (List<LinkedHashMap>) response.get("resourceSpecification");

	// Getting childResourceSpecifications from resourceSpecifications that we got
	// from response
	List childResourceSpecifications = (ArrayList<VFModelInfo>) (resourceSpecifications.get(0))
			.get("childResourceSpecification");

	// Asserting childResourceSpecifications with our vfModelInfoListTestData ::
	// CSAR has two vfModules

	for (int i = 0; i < vfModelInfoListTestData.size(); i++) {
		assertThat(childResourceSpecifications.get(i)).hasFieldOrPropertyWithValue("modelName",
				vfModelInfoListTestData.get(i).getModelName());
		assertThat(childResourceSpecifications.get(i)).hasFieldOrPropertyWithValue("modelUuid",
				vfModelInfoListTestData.get(i).getModelUuid());
		assertThat(childResourceSpecifications.get(i)).hasFieldOrPropertyWithValue("modelInvariantUuid",
				vfModelInfoListTestData.get(i).getModelInvariantUuid());
		assertThat(childResourceSpecifications.get(i)).hasFieldOrPropertyWithValue("modelVersion",
				vfModelInfoListTestData.get(i).getModelVersion());
		assertThat(childResourceSpecifications.get(i)).hasFieldOrPropertyWithValue("modelCustomizationUuid",
				vfModelInfoListTestData.get(i).getModelCustomizationUuid());
	}

    }

    @Test
    public void testBuildAndSaveResponseWithSdcToscaParserWithInstanceSpecification() {

	ClassLoader classLoader = getClass().getClassLoader();

	// Adding Path to TOSCA File to provide as parameter
	Path path = new File(classLoader.getResource("toscafile/service-VlbService-csar.csar").getFile()).toPath()
			.toAbsolutePath();

	// Creating response to provide as parameter
	LinkedHashMap response = new LinkedHashMap();
	response.put("version", "1.0");
	response.put("name", "VLB_Service");
	response.put("description", "VLB_Service");
	response.put("id", "82c9fbb4-656c-4973-8c7f-172b22b5fa8f");

	// Resources
	List<LinkedHashMap> resources = new ArrayList<>();
	LinkedHashMap resource1 = new LinkedHashMap();
	resource1.put("id", "35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc");
	resources.add(resource1);

	// instanceSpecification Test Data
	Map instanceSpecificationTestData = new LinkedHashMap<>();
	instanceSpecificationTestData.put("cloud_env", "openstack");
	instanceSpecificationTestData.put("demo_artifacts_version", "1.2.1");
	instanceSpecificationTestData.put("install_script_version", "1.2.1");
	instanceSpecificationTestData.put("onap_private_net_id", "09407156-5e6e-45a7-b4aa-6eeb7ad4aba9");
	instanceSpecificationTestData.put("onap_private_subnet_id", "8c6df8fa-2735-49ad-ba04-24701d85ba79");
	instanceSpecificationTestData.put("pub_key",
			"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC/EnxIi7fcHMEi9VPtCGCOpQYblj9r0M/CaD5U15Cb5qHzcHiPtJpVsDMlPGzN9VHxWZG6FqQv1s6oE+PmG1xeahhb+ofrY6s8zvlUCcWGIo/bPexzb2ErvkGyd+1tQo9oLrxNdUG0xeWUX3oFkiw3RBRyxf9n4E5ajZr4cEFQ0sqJkslj87XViw/h555ydIYTY5cPNmIlsIXTObC/2z3muVWYUzaaZE8omfYJE442+UhYLHgb7Cl1JMk/SNu/r+bLrsXeBSPB+/bxVKqjpd659AQ7GRNXvBrgfq6EKNiVjrI76AbpeTM2D/LXbENuUUkvJBWptSd0gPAGkEyc9w2n");
	instanceSpecificationTestData.put("public_net_id",
			"60dc8a1c-86b8-4cc4-b5c8-9b0272113c1f0f1c389d-e9db-4c14-b3a2-11dca2d104ed");

	// Resources to put in response as resourceSpecification
	response.put("resourceSpecification", resources);

	// List<LinkedHashMap> vfModelInfoListTestData = new ArrayList();

	try {
		toscaInfosProcessor.buildAndSaveResponseWithSdcToscaParser(path, response);
	} catch (SdcToscaParserException e) {
		throw new TechnicalException("unable to build response from tosca csar using sdc-parser : "
				+ path.toString() + " " + e.getMessage());
	}

	// Getting
	List<LinkedHashMap> resourceSpecifications = (List<LinkedHashMap>) response.get("resourceSpecification");

	Map instanceSpecification = (HashMap) (resourceSpecifications.get(0)).get("InstanceSpecification");

	// Test against test data and returned response's data for instanceSpecification
	// instanceSpecificationTestData = new HashMap();
	assertThat(instanceSpecificationTestData).isEqualTo(instanceSpecification);

    }
}
