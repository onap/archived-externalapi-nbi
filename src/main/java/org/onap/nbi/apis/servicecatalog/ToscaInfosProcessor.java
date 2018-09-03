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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ToscaInfosProcessor {

    @Autowired
    SdcClient sdcClient;

    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

    private static final Logger LOGGER = LoggerFactory.getLogger(ToscaInfosProcessor.class);

    public void buildResponseWithToscaInfos(Map toscaInfosTopologyTemplate,
            Map serviceCatalogResponse) {
        if (toscaInfosTopologyTemplate.get("inputs") != null) {
            ArrayList serviceSpecCharacteristic = new ArrayList();
            LinkedHashMap toscaInfos = (LinkedHashMap) toscaInfosTopologyTemplate.get("inputs");
            Set<Entry<String, LinkedHashMap>> stringLinkedHashMapEntry = (Set<Entry<String, LinkedHashMap>>) toscaInfos
                .entrySet();

            for (Map.Entry<String,LinkedHashMap> key :stringLinkedHashMapEntry) {
                String keyString = key.getKey();
                LinkedHashMap inputParameter = key.getValue();
                LinkedHashMap mapParameter = new LinkedHashMap();
                String parameterType = (String) inputParameter.get("type");
                mapParameter.put("name", keyString);
                mapParameter.put("description", inputParameter.get("description"));
                mapParameter.put("valueType", parameterType);
                mapParameter.put("@type", "ONAPserviceCharacteristic");
                mapParameter.put("required", inputParameter.get("required"));
                mapParameter.put("status", inputParameter.get("status"));
                List<LinkedHashMap> serviceSpecCharacteristicValues =
                    buildServiceSpecCharacteristicsValues(inputParameter, parameterType);
                mapParameter.put("serviceSpecCharacteristicValue", serviceSpecCharacteristicValues);
                serviceSpecCharacteristic.add(mapParameter);
            }

            serviceCatalogResponse.put("serviceSpecCharacteristic", serviceSpecCharacteristic);
        }
        LinkedHashMap nodeTemplate = (LinkedHashMap) toscaInfosTopologyTemplate.get("node_templates");

        List<LinkedHashMap> resourceSpecifications =
                (List<LinkedHashMap>) serviceCatalogResponse.get("resourceSpecification");
        for (LinkedHashMap resourceSpecification : resourceSpecifications) {
            if(resourceSpecification.get("id")!=null){
                String id = (String) resourceSpecification.get("id");
                LOGGER.debug("get tosca infos for service id: {}", id);
                LinkedHashMap toscaInfosFromResourceId = getToscaInfosFromResourceUUID(nodeTemplate, id);
                if (toscaInfosFromResourceId != null && toscaInfosFromResourceId.get("customizationUUID")!=null) {
                    resourceSpecification.put("modelCustomizationId", toscaInfosFromResourceId.get("customizationUUID"));
                }
            }
        }
    }

    private List<LinkedHashMap> buildServiceSpecCharacteristicsValues(LinkedHashMap parameter, String parameterType) {
        List<LinkedHashMap> serviceSpecCharacteristicValues = new ArrayList<>();
        if (!"map".equalsIgnoreCase(parameterType) && !"list".equalsIgnoreCase(parameterType)) {
            LOGGER.debug("get tosca infos for serviceSpecCharacteristicValues of type map or string : {}", parameter);
            Object aDefault = parameter.get("default");
            if (parameter.get("entry_schema") != null) {
                ArrayList entrySchema = (ArrayList) parameter.get("entry_schema");
                if (!CollectionUtils.isEmpty(entrySchema)) {
                    buildCharacteristicValuesFormShema(parameterType, serviceSpecCharacteristicValues, aDefault,
                            entrySchema);
                }
            }
        }
        return serviceSpecCharacteristicValues;
    }

    private void buildCharacteristicValuesFormShema(String parameterType,
            List<LinkedHashMap> serviceSpecCharacteristicValues, Object aDefault, ArrayList entrySchema) {
        LinkedHashMap constraints = (LinkedHashMap) entrySchema.get(0);
        if (constraints != null) {
            ArrayList constraintsList = (ArrayList) constraints.get("constraints");
            if (!CollectionUtils.isEmpty(constraintsList)) {
                LinkedHashMap valuesMap = (LinkedHashMap) constraintsList.get(0);
                if (valuesMap != null) {
                    List<Object> values = (List<Object>) valuesMap.get("valid_values");
                    for (Object value : values) {
                        String stringValue = value.toString();
                        LinkedHashMap serviceSpecCharacteristicValue = new LinkedHashMap();
                        serviceSpecCharacteristicValue.put("isDefault",
                                aDefault != null && aDefault.toString().equals(stringValue));
                        serviceSpecCharacteristicValue.put("value", stringValue);
                        serviceSpecCharacteristicValue.put("valueType", parameterType);
                        serviceSpecCharacteristicValues.add(serviceSpecCharacteristicValue);
                    }
                }
            }
        }
    }


    private LinkedHashMap getToscaInfosFromResourceUUID(LinkedHashMap nodeTemplates, String name) {
        if(nodeTemplates!=null) {
            for (Object nodeTemplateObject : nodeTemplates.values()) {
                LinkedHashMap nodeTemplate = (LinkedHashMap) nodeTemplateObject;
                LinkedHashMap metadata = (LinkedHashMap) nodeTemplate.get("metadata");
                if(metadata.get("UUID")!=null && metadata.get("type")!=null) {
                    String metadataUUID = (String) metadata.get("UUID");
                    String metadataType = (String) metadata.get("type");
                    if ("VF".equalsIgnoreCase(metadataType) && name!=null &&  name.equalsIgnoreCase(metadataUUID)) {
                        return metadata;
                    }
                }
            }
        }
        return null;
    }


    public Map getToscaInfos(Map sdcResponse) {

        LinkedHashMap topologyTemplate = null;

        String toscaModelUrl = (String) sdcResponse.get("toscaModelURL");
        String serviceId = (String) sdcResponse.get("id");
        File toscaFile = sdcClient.callGetWithAttachment(toscaModelUrl);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String tempFolderName = serviceId + timestamp;
        File folderTemp = null;

        try {
            unZipArchive(toscaFile.getName(), tempFolderName);
            folderTemp = new File(tempFolderName);
            LOGGER.debug("temp folder for tosca files : " + folderTemp.getName());

            LinkedHashMap toscaMetaFileHashMap = parseToscaFile(tempFolderName + "/TOSCA-Metadata/TOSCA.meta");
            topologyTemplate = getToscaTopologyTemplateNode(tempFolderName, toscaMetaFileHashMap);
            return topologyTemplate;
        } catch (TechnicalException e) {
            LOGGER.error("unable to parse tosca file for id : " + serviceId, e);
            return  topologyTemplate;
        }
        finally {
            deleteTempFiles(serviceId, toscaFile, folderTemp);
        }

    }

    private LinkedHashMap getToscaTopologyTemplateNode(String tempFolderName,LinkedHashMap toscaMetaFileHashMap) {
        LinkedHashMap topologyTemplate = null;
        if (toscaMetaFileHashMap.get("Entry-Definitions") != null) {
            String toscaFilePath = (String) toscaMetaFileHashMap.get("Entry-Definitions");
            LinkedHashMap toscaFileHashMap = parseToscaFile(tempFolderName + "/" + toscaFilePath);
            if (toscaFileHashMap.get("topology_template") != null) {
                topologyTemplate = (LinkedHashMap) toscaFileHashMap.get("topology_template");
            } else {
                LOGGER.error("no Entry-Definitions node in TOSCA.meta");
            }
        } else {
            LOGGER.error("no topology_template node in tosca file");
        }
        return topologyTemplate;
    }


    private void deleteTempFiles(String serviceId, File toscaFile, File folderTemp) {
        try {
            if(folderTemp!=null){
                LOGGER.debug("deleting temp folder for tosca files : " + folderTemp.getName());
                FileUtils.deleteDirectory(folderTemp);
            }
            LOGGER.debug("deleting tosca archive : " + toscaFile.getName());
            FileUtils.forceDelete(toscaFile);
        } catch (IOException e) {
            LOGGER.error("unable to delete temp directory tosca file for id : " + serviceId, e);
        }
    }

    private LinkedHashMap parseToscaFile(String fileName) {

        File toscaFile = new File(fileName);
        if (!toscaFile.exists()) {
            throw new TechnicalException("unable to find  file : " + fileName);
        }
        try {
            return (LinkedHashMap) mapper.readValue(toscaFile, Object.class);
        } catch (IOException e) {
            LOGGER.warn("unable to parse tosca file : " + fileName, e);
            throw new TechnicalException("Unable to parse tosca file : " + fileName);

        } catch (NullPointerException e) {
            LOGGER.warn("unable to find tosca file : " + fileName, e);
            throw new TechnicalException("unable to find tosca file : " + fileName);
        }
    }


    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param outputFolder zip file output folder
     */
    private void unZipArchive(String zipFile, String outputFolder) {

        byte[] buffer = new byte[1024];

        try {

            // create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // get the zip file content
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
                // get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {

                    String fileName = ze.getName();
                    File newFile = new File(outputFolder + File.separator + fileName);

                    LOGGER.debug("File to unzip : " + newFile.getAbsoluteFile());

                    // create all non exists folders
                    // else you will hit FileNotFoundException for compressed folder
                    new File(newFile.getParent()).mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {

                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    ze = zis.getNextEntry();
                }
                zis.closeEntry();
            }

            LOGGER.debug("Done");

        } catch (IOException ex) {
            LOGGER.error("Error while unzipping ToscaModel archive from ONAP", ex);
            throw new TechnicalException("Error while unzipping ToscaModel archive from ONAP");
        }
    }


}
