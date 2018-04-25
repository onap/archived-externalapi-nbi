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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Test;
import org.onap.nbi.exceptions.TechnicalException;


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