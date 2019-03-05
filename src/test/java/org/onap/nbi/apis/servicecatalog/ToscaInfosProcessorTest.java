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
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Test;
import org.onap.nbi.exceptions.TechnicalException;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


public class ToscaInfosProcessorTest {

  final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

  ToscaInfosProcessor toscaInfosProcessor = new ToscaInfosProcessor();



  @Test
  public void buildResponseWithSdcToscaParser() {

    ClassLoader classLoader = getClass().getClassLoader();
    Path path = new File(
        classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile())
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
    assertThat(serviceSpecCharacteristic.get(0).get("name"))
        .isEqualTo("sdwanconnectivity0_topology");
    assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
    assertThat(serviceSpecCharacteristic.get(0).get("required")).isEqualTo(true);
    assertThat(serviceSpecCharacteristic.get(1).get("name")).isEqualTo("sdwanconnectivity0_name");
    assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
    assertThat(serviceSpecCharacteristic.get(1).get("required")).isEqualTo(true);
    assertThat(resources.get(0).get("modelCustomizationId"))
        .isEqualTo("94ec574b-2306-4cbd-8214-09662b040f73");
    assertThat(resources.get(1).get("modelCustomizationId"))
        .isEqualTo("a7baba5d-6ac3-42b5-b47d-070841303ab1");

  }

  @Test
  public void buildResponseWithSdcToscaParserWithDefaultInputs() {

    ClassLoader classLoader = getClass().getClassLoader();
    Path path = new File(
        classLoader.getResource("toscafile/service-Sotnvpninfraservice-csar.csar").getFile())
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
    assertThat(resources.get(0).get("modelCustomizationId"))
        .isEqualTo("b44071c8-04fd-4d6b-b6af-772cbfaa1129");
    assertThat(resources.get(1).get("modelCustomizationId"))
        .isEqualTo("c3612284-6c67-4d8c-8b41-b699cc90e76d");
    assertThat(serviceSpecCharacteristic.get(12).get("serviceSpecCharacteristicValue")).isNull();
    assertThat(serviceSpecCharacteristic.get(13).get("serviceSpecCharacteristicValue")).isNotNull();
  }

  @Test
  public void buildResponseWithSdcToscaParserwithMetaDataMisMatch() {

    ClassLoader classLoader = getClass().getClassLoader();
    Path path = new File(
        classLoader.getResource("toscafile/service-Sdwanvpninfraservice-csar.csar").getFile())
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
    assertThat(serviceSpecCharacteristic.get(0).get("name"))
        .isEqualTo("sdwanconnectivity0_topology");
    assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
    assertThat(serviceSpecCharacteristic.get(0).get("required")).isEqualTo(true);
    assertThat(serviceSpecCharacteristic.get(1).get("name")).isEqualTo("sdwanconnectivity0_name");
    assertThat(serviceSpecCharacteristic.get(1).get("valueType")).isEqualTo("string");
    assertThat(serviceSpecCharacteristic.get(1).get("required")).isEqualTo(true);
    // Check that resources cannot be found in the TOSCA template
    assertThat(resources.get(0).get("modelCustomizationId")).isNull();
    assertThat(resources.get(1).get("modelCustomizationId")).isNull();

  }
}
