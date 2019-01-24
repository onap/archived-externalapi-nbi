/**
 * Copyright (c) 2018 Orange
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
package org.onap.nbi.apis.servicecatalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.Test;
import org.onap.nbi.exceptions.TechnicalException;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;


public class ToscaInfosProcessorTest {

    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

    ToscaInfosProcessor toscaInfosProcessor = new ToscaInfosProcessor();


    private LinkedHashMap parseToscaFile(String fileName) {

        File toscaFile = new File(fileName);
        if (!toscaFile.exists()) {
            throw new TechnicalException("unable to find  file : " + fileName);
        }
        try {
            return (LinkedHashMap) mapper.readValue(toscaFile, Object.class);
        } catch (IOException e) {
            throw new TechnicalException("Unable to parse tosca file : " + fileName);

        } catch (NullPointerException e) {
            throw new TechnicalException("unable to find tosca file : " + fileName);
        }
    }


    @Test
    public void buildResponseWithToscaInfos() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("toscafile/service-TestNetwork-template.yml").getFile());
        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "e2b12ac6-cbb6-4517-9c58-b846d1f68caf");
        resources.add(resource1);
        LinkedHashMap toscaFile = parseToscaFile(file.getPath());
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification", resources);
        toscaInfosProcessor.buildResponseWithToscaInfos((LinkedHashMap) toscaFile.get("topology_template"), response);

        resources = (List<LinkedHashMap>) response.get("resourceSpecification");
        assertNull(resources.get(0).get("modelCustomizationId"));
        assertNull(resources.get(0).get("modelCustomizationName"));

    }

    @Test
    public void buildResponseWithSdcToscaParser() {

        ClassLoader classLoader = getClass().getClassLoader();
        Path path = new File(classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile()).toPath().toAbsolutePath();
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
        }
        catch(SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : " + path.toString()+" "+e.getMessage());
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
        Path path = new File(classLoader.getResource("toscafile/service-Sotnvpninfraservice-csar.csar").getFile()).toPath().toAbsolutePath();
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
        }
        catch(SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : " + path.toString()+" "+e.getMessage());
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
        Path path = new File(classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile()).toPath().toAbsolutePath();
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
        }
        catch(SdcToscaParserException e) {
            throw new TechnicalException("unable to build response from tosca csar using sdc-parser : " + path.toString()+" "+e.getMessage());
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
    public void buildResponseWithToscaInfosOk() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("toscafile/service-VfwService2vfBased-template.yml").getFile());
        List<LinkedHashMap> resources = new ArrayList<>();
        LinkedHashMap resource1 = new LinkedHashMap();
        resource1.put("id", "e2b12ac6-cbb6-4517-9c58-b846d1f68caf");
        resources.add(resource1);
        LinkedHashMap toscaFile = parseToscaFile(file.getPath());
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification", resources);
        toscaInfosProcessor.buildResponseWithToscaInfos((LinkedHashMap) toscaFile.get("topology_template"), response);

        ArrayList toscaInfos = (ArrayList) response.get("serviceSpecCharacteristic");
        assertThat(toscaInfos.size()).isEqualTo(4);

        for (Object toscaInfo : toscaInfos) {
            LinkedHashMap info = (LinkedHashMap) toscaInfo;
            if (((String) info.get("name")).equalsIgnoreCase("fortigate_image_url")) {
                assertThat(info.get("name")).isEqualTo("fortigate_image_url");
                assertThat(info.get("description")).isNull();
                assertThat(info.get("valueType")).isEqualTo("string");
                assertThat(info.get("@type")).isEqualTo("ONAPserviceCharacteristic");
                assertThat(info.get("required")).isEqualTo(false);
                assertThat(info.get("status")).isNull();
                assertThat(((ArrayList) info.get("serviceSpecCharacteristicValue")).size()).isEqualTo(0);

            }

            if (((String) info.get("name")).equalsIgnoreCase("flavor")) {
                assertThat(info.get("name")).isEqualTo("flavor");
                assertThat(info.get("description")).isNull();
                assertThat(info.get("valueType")).isEqualTo("string");
                assertThat(info.get("@type")).isEqualTo("ONAPserviceCharacteristic");
                assertThat(info.get("required")).isNull();
                assertThat(info.get("status")).isNull();
                assertThat(((ArrayList) info.get("serviceSpecCharacteristicValue")).size()).isEqualTo(0);

            }

            if (((String) info.get("name")).equalsIgnoreCase("external_network_name")) {
                assertThat(info.get("name")).isEqualTo("external_network_name");
                assertThat(info.get("description")).isNull();
                assertThat(info.get("valueType")).isEqualTo("string");
                assertThat(info.get("@type")).isEqualTo("ONAPserviceCharacteristic");
                assertThat(info.get("required")).isNull();
                assertThat(info.get("status")).isEqualTo("inactive");
                ;
                assertThat(((ArrayList) info.get("serviceSpecCharacteristicValue")).size()).isEqualTo(0);

            }

            if (((String) info.get("name")).equalsIgnoreCase("cpus")) {
                assertThat(info.get("name")).isEqualTo("cpus");
                assertThat(info.get("description")).isEqualTo("Number of CPUs for the server.");
                assertThat(info.get("valueType")).isEqualTo("integer");
                assertThat(info.get("@type")).isEqualTo("ONAPserviceCharacteristic");
                assertThat(info.get("required")).isNull();
                assertThat(info.get("status")).isNull();
                ;
                assertThat(((ArrayList) info.get("serviceSpecCharacteristicValue")).size()).isEqualTo(4);
                ArrayList serviceSpecCharacteristicValues = (ArrayList) info.get("serviceSpecCharacteristicValue");

                for (Object serviceSpecCharacteristicValue : serviceSpecCharacteristicValues) {
                    LinkedHashMap serviceSpecValue = (LinkedHashMap) serviceSpecCharacteristicValue;
                    if (((String) serviceSpecValue.get("value")).equalsIgnoreCase("1")) {
                        assertThat(serviceSpecValue.get("isDefault")).isEqualTo(false);
                        assertThat(serviceSpecValue.get("value")).isEqualTo("1");
                        assertThat(serviceSpecValue.get("valueType")).isEqualTo("integer");
                    }
                    if (((String) serviceSpecValue.get("value")).equalsIgnoreCase("2")) {
                        assertThat(serviceSpecValue.get("isDefault")).isEqualTo(true);
                        assertThat(serviceSpecValue.get("value")).isEqualTo("2");
                        assertThat(serviceSpecValue.get("valueType")).isEqualTo("integer");
                    }
                    if (((String) serviceSpecValue.get("value")).equalsIgnoreCase("3")) {
                        assertThat(serviceSpecValue.get("isDefault")).isEqualTo(false);
                        assertThat(serviceSpecValue.get("value")).isEqualTo("3");
                        assertThat(serviceSpecValue.get("valueType")).isEqualTo("integer");
                    }
                    if (((String) serviceSpecValue.get("value")).equalsIgnoreCase("4")) {
                        assertThat(serviceSpecValue.get("isDefault")).isEqualTo(false);
                        assertThat(serviceSpecValue.get("value")).isEqualTo("4");
                        assertThat(serviceSpecValue.get("valueType")).isEqualTo("integer");
                    }

                }


            }

        }


    }
}