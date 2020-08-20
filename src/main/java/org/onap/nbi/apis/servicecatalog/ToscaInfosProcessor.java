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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.onap.nbi.apis.serviceorder.model.consumer.VFModelInfo;
import org.onap.sdc.tosca.parser.api.IEntityDetails;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.elements.queries.EntityQuery;
import org.onap.sdc.tosca.parser.elements.queries.TopologyTemplateQuery;
import org.onap.sdc.tosca.parser.enums.SdcTypes;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.SdcPropertyNames;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.sdc.toscaparser.api.functions.GetInput;
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

    private Set<String> vnfInstanceParams = new HashSet<String>(Arrays.asList("onap_private_net_id",
        "onap_private_subnet_id", "pub_key", "sec_group", "install_script_version", "demo_artifacts_version",
        "cloud_env", "public_net_id", "aic-cloud-region", "image_name", "flavor_name"));

    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

    private static final Logger LOGGER = LoggerFactory.getLogger(ToscaInfosProcessor.class);

    public void buildResponseWithSdcToscaParser(Path path, Map serviceCatalogResponse) throws SdcToscaParserException {

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

		List<IEntityDetails> vfEntityList = sdcCsarHelper.getEntity(EntityQuery.newBuilder(SdcTypes.VF).build(),
				TopologyTemplateQuery.newBuilder(SdcTypes.SERVICE).build(), false);

		Map<String, org.onap.sdc.toscaparser.api.Property> groupProperties = null;
		Map<String, String> listOfInstanceParameters = new HashMap<>();
		if (!vfEntityList.isEmpty()) {

			IEntityDetails iEntityDetails = vfEntityList.get(0);
			groupProperties = iEntityDetails.getProperties();

			for (String key : groupProperties.keySet()) {
				org.onap.sdc.toscaparser.api.Property property = groupProperties.get(key);
				String paramName = property.getName();
				if (paramName != null) {
					if (vnfInstanceParams.stream()
							.filter(vnfInstanceParam -> vnfInstanceParam.equalsIgnoreCase(paramName)).findFirst()
							.isPresent()) {
						listOfInstanceParameters.put(paramName, property.getValue().toString());
					}
				}
			}

		}

		// it will build Entity as VfModules
		List<IEntityDetails> vfModuleEntityList = sdcCsarHelper.getEntity(
				EntityQuery.newBuilder("org.openecomp.groups.VfModule").build(),
				TopologyTemplateQuery.newBuilder(SdcTypes.SERVICE)
						.customizationUUID(SdcPropertyNames.PROPERTY_NAME_CUSTOMIZATIONUUID).build(),
				false);
		List<VFModelInfo> listOfVfModelInfo = new ArrayList<>();

		if (!vfModuleEntityList.isEmpty()) {
			// Fetching vfModule metadata in a loop
			for (IEntityDetails vfModuleEntity : vfModuleEntityList) {
				VFModelInfo vfModel = new VFModelInfo();
				Metadata vfMetadata = vfModuleEntity.getMetadata();
				// Preparing VFModel
				vfModel.setModelInvariantUuid(
						testNull(vfMetadata.getValue(SdcPropertyNames.PROPERTY_NAME_VFMODULEMODELINVARIANTUUID)));
				vfModel.setModelName(testNull(vfMetadata.getValue(SdcPropertyNames.PROPERTY_NAME_VFMODULEMODELNAME)));
				vfModel.setModelUuid(testNull(vfMetadata.getValue(SdcPropertyNames.PROPERTY_NAME_VFMODULEMODELUUID)));
				vfModel.setModelVersion(
						testNull(vfMetadata.getValue(SdcPropertyNames.PROPERTY_NAME_VFMODULEMODELVERSION)));
				vfModel.setModelCustomizationUuid(testNull(vfMetadata.getValue("vfModuleModelCustomizationUUID")));

				// Adding it to the list
				listOfVfModelInfo.add(vfModel);
			}
		}

		Map<String, Model> definitions = new HashMap<String, Model>();
		Model model = new ModelImpl();

		if (!inputs.isEmpty() && inputs.size() > 0) {
			for (Input input : inputs) {
				Property property = null;
				if (input.getType().equals("list") || input.getType().equals("map"))
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
		serviceSpecificationDBManager.saveSpecificationInputSchema(svcCharacteristicsJson, serviceCatalogResponse);

		Metadata serviceMetadata = sdcCsarHelper.getServiceMetadata();
		String instantationType = serviceMetadata.getValue("instantiationType");
		serviceCatalogResponse.put("instantiationType", instantationType);

		LinkedHashMap inputSchemaRef = new LinkedHashMap();
		// use object to match examples in Specifications
		inputSchemaRef.put("valueType", "object");
		inputSchemaRef.put("@schemaLocation",
				"/serviceSpecification/" + serviceCatalogResponse.get("id") + "/specificationInputSchema");
		inputSchemaRef.put("@type", serviceCatalogResponse.get("name") + "_ServiceCharacteristic");

		LinkedHashMap serviceSpecCharacteristic = new LinkedHashMap();
		serviceSpecCharacteristic.put("name", serviceCatalogResponse.get("name") + "_ServiceCharacteristics");
		serviceSpecCharacteristic.put("description",
				"This object describes all the inputs needed from the client to interact with the "
						+ serviceCatalogResponse.get("name") + " Service Topology");
		serviceSpecCharacteristic.put("valueType", "object");
		serviceSpecCharacteristic.put("@type", "ONAPServiceCharacteristic");
		serviceSpecCharacteristic.put("@schemaLocation", "null");
		serviceSpecCharacteristic.put("serviceSpecCharacteristicValue", inputSchemaRef);

		serviceCatalogResponse.put("serviceSpecCharacteristic", serviceSpecCharacteristic);

		List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();
		List<LinkedHashMap> resourceSpecifications = (List<LinkedHashMap>) serviceCatalogResponse
				.get("resourceSpecification");
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
				if (!vfModuleEntityList.isEmpty()) {
					resourceSpecification.put("childResourceSpecification", listOfVfModelInfo);
				}
				HashMap<String, Object> serviceInstanceParams = getServiceLevelInstanceParams(inputs);
				resourceSpecification.put("serviceInstanceParams", serviceInstanceParams);
				HashMap<String, Object> vnfInstanceParams = getUserDefinedVFLevelInstanceParams(groupProperties, listOfInstanceParameters);
				resourceSpecification.put("InstanceSpecification", vnfInstanceParams);
			}
		}
	}
    
 // Get List of Service Level InputParams as Key Value
 	private HashMap<String, Object> getServiceLevelInstanceParams(List<Input> listOfServiceLevelInputs) {

 		HashMap<String, Object> serviceLevelInstanceParams = new HashMap<>();

 		for (Input input : listOfServiceLevelInputs) {
 			serviceLevelInstanceParams.put(input.getName(), input.getDefault());
 		}

 		return serviceLevelInstanceParams;
 	}
 	
 	private HashMap<String, Object> getUserDefinedVFLevelInstanceParams(
 			Map<String, org.onap.sdc.toscaparser.api.Property> groupProperties, Map listOfVFLevelInputs) {

 		HashMap<String, Object> vnfLevelInstanceParams = new HashMap<>();
 		
 		for (Entry<String, org.onap.sdc.toscaparser.api.Property> entry : groupProperties.entrySet()) {

 			org.onap.sdc.toscaparser.api.Property property = entry.getValue();
 			
 			if ((property.getValue().getClass() == GetInput.class)) {
 				GetInput getInput = (GetInput) property.getValue();
 				listOfVFLevelInputs.put(getInput.getInputName(), getInput.result());
 				listOfVFLevelInputs.remove(property.getName());
 			}
 		}

 		return (HashMap<String, Object>) listOfVFLevelInputs;		
 	}


    private static String testNull(Object object) {
        if (object == null) {
          return "NULL";
        } else if (object instanceof Integer) {
          return object.toString();
        } else if (object instanceof String) {
          return (String) object;
        } else {
          return "Type not recognized";
        }
    }

}