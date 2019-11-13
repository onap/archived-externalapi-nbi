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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.parameters.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.util.Json;

@Service
public class ToscaInfosProcessor {

  @Autowired
  SdcClient sdcClient;

  @Autowired
  private ServiceSpecificationDBManager serviceSpecificationDBManager;

  final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

  private static final Logger LOGGER = LoggerFactory.getLogger(ToscaInfosProcessor.class);

  public void buildResponseWithSdcToscaParser(Path path, Map serviceCatalogResponse)
      throws SdcToscaParserException {

    SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
    ISdcCsarHelper sdcCsarHelper = factory.getSdcCsarHelper(path.toFile().getAbsolutePath(), false);
    List<Input> inputs = sdcCsarHelper.getServiceInputs();
    if (inputs != null && inputs.size() > 0) {
      ArrayList serviceSpecCharacteristic = new ArrayList();
      for (Input input : inputs) {
        LinkedHashMap mapParameter = new LinkedHashMap();
        mapParameter.put("name", input.getName());
        mapParameter.put("description", input.getDescription());
        mapParameter.put("valueType", input.getType());
        mapParameter.put("@type", "ONAPserviceCharacteristic");
        mapParameter.put("required", input.isRequired());
        mapParameter.put("status", null);
        mapParameter.put("serviceSpecCharacteristicValue", null);
        // If this Input has a default value, then put it in serviceSpecCharacteristicValue
        if (input.getDefault() != null) {
          List<LinkedHashMap> serviceSpecCharacteristicValues =
              buildServiceSpecCharacteristicsValuesFromSdc(input);
          mapParameter.put("serviceSpecCharacteristicValue", serviceSpecCharacteristicValues);
        }
        serviceSpecCharacteristic.add(mapParameter);
      }
      serviceCatalogResponse.put("serviceSpecCharacteristic", serviceSpecCharacteristic);
    }
    List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();

    List<LinkedHashMap> resourceSpecifications =
        (List<LinkedHashMap>) serviceCatalogResponse.get("resourceSpecification");
    for (LinkedHashMap resourceSpecification : resourceSpecifications) {
      if (resourceSpecification.get("id") != null) {
        String id = (String) resourceSpecification.get("id");
        LOGGER.debug("get tosca infos for service id: {}", id);
        NodeTemplate nodeTemplate = null;
        for (NodeTemplate node : nodeTemplates) {
          if (node.getMetaData().getValue("UUID").equals(id)) {
            nodeTemplate = node;
            break;
          }
        }
        if (nodeTemplate == null)
          continue;
        resourceSpecification.put("modelCustomizationId",
            sdcCsarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
      }
    }
  }

  private List<LinkedHashMap> buildServiceSpecCharacteristicsValuesFromSdc(Input input) {

    List<LinkedHashMap> serviceSpecCharacteristicValues = new ArrayList<>();
    LinkedHashMap serviceSpecCharacteristicValue = new LinkedHashMap();

    serviceSpecCharacteristicValue.put("isDefault", true);
    serviceSpecCharacteristicValue.put("value", input.getDefault());
    serviceSpecCharacteristicValue.put("valueType", input.getType());
    serviceSpecCharacteristicValues.add(serviceSpecCharacteristicValue);

    return serviceSpecCharacteristicValues;
  }

  public void buildAndSaveResponseWithSdcToscaParser(Path path, Map serviceCatalogResponse)
      throws SdcToscaParserException {

    SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
    ISdcCsarHelper sdcCsarHelper = factory.getSdcCsarHelper(path.toFile().getAbsolutePath(), false);
    List<Input> inputs = sdcCsarHelper.getServiceInputs();

    Map<String, Model> definitions = new HashMap<String, Model>();
    Model model = new ModelImpl();

    if (inputs != null && inputs.size() > 0) {
      for (Input input : inputs) {
    	Property property = null;
    	if (input.getType().equals("list") ||  input.getType().equals("map"))
    		property = PropertyBuilder.build("array", null, null); 
    	else
    		property = PropertyBuilder.build(input.getType(), null, null);

    	property.setDescription(input.getDescription());
        property.setRequired(input.isRequired());

        if (input.getDefault() != null) {
          property.setDefault(input.getDefault().toString());
        }
        ((ModelImpl) model).addProperty(input.getName(), property);
      }
      definitions.put("ServiceCharacteristics", model);

    }

    String svcCharacteristicsJson = Json.pretty(definitions);
    serviceSpecificationDBManager.saveSpecificationInputSchema(svcCharacteristicsJson,
        serviceCatalogResponse);

    LinkedHashMap inputSchemaRef = new LinkedHashMap();
    // use object to match examples in Specifications
    inputSchemaRef.put("valueType", "object");
    inputSchemaRef.put("@schemaLocation",
        "/serviceSpecification/" + serviceCatalogResponse.get("id") + "/specificationInputSchema");
    inputSchemaRef.put("@type", serviceCatalogResponse.get("name") + "_ServiceCharacteristic");

    LinkedHashMap serviceSpecCharacteristic = new LinkedHashMap();
    serviceSpecCharacteristic.put("name",
        serviceCatalogResponse.get("name") + "_ServiceCharacteristics");
    serviceSpecCharacteristic.put("description",
        "This object describes all the inputs needed from the client to interact with the "
            + serviceCatalogResponse.get("name") + " Service Topology");
    serviceSpecCharacteristic.put("valueType", "object");
    serviceSpecCharacteristic.put("@type", "ONAPServiceCharacteristic");
    serviceSpecCharacteristic.put("@schemaLocation", "null");
    serviceSpecCharacteristic.put("serviceSpecCharacteristicValue", inputSchemaRef);

    serviceCatalogResponse.put("serviceSpecCharacteristic", serviceSpecCharacteristic);

    List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();
    List<LinkedHashMap> resourceSpecifications =
        (List<LinkedHashMap>) serviceCatalogResponse.get("resourceSpecification");
    for (LinkedHashMap resourceSpecification : resourceSpecifications) {
      if (resourceSpecification.get("id") != null) {
        String id = (String) resourceSpecification.get("id");
        LOGGER.debug("get tosca infos for service id: {}", id);
        NodeTemplate nodeTemplate = null;
        for (NodeTemplate node : nodeTemplates) {
          if (node.getMetaData().getValue("UUID").equals(id)) {
            nodeTemplate = node;
            break;
          }
        }
        if (nodeTemplate == null)
          continue;
        resourceSpecification.put("modelCustomizationId",
            sdcCsarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
      }
    }
  }

}
