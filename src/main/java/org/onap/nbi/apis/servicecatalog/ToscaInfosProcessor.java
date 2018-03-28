package org.onap.nbi.apis.servicecatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class ToscaInfosProcessor {

    @Autowired
    SdcClient sdcClient;

    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind

    private static final Logger LOGGER = LoggerFactory.getLogger(ToscaInfosProcessor.class);

    public void buildResponseWithToscaInfos(LinkedHashMap toscaInfosTopologyTemplate,
            LinkedHashMap serviceCatalogResponse) {
        if (toscaInfosTopologyTemplate.get("inputs") != null) {
            ArrayList serviceSpecCharacteristic = new ArrayList();
            LinkedHashMap toscaInfos = (LinkedHashMap) toscaInfosTopologyTemplate.get("inputs");
            for (Object key : toscaInfos.keySet()) {
                String keyString = (String) key;
                LinkedHashMap inputParameter = (LinkedHashMap) toscaInfos.get(key);
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
        LinkedHashMap node_templates = (LinkedHashMap) toscaInfosTopologyTemplate.get("node_templates");

        List<LinkedHashMap> resourceSpecifications =
                (List<LinkedHashMap>) serviceCatalogResponse.get("resourceSpecification");
        for (LinkedHashMap resourceSpecification : resourceSpecifications) {
            String id = (String) resourceSpecification.get("id");
            LOGGER.debug("get tosca infos for service id: " + id);
            LinkedHashMap toscaInfosFromResourceId = getToscaInfosFromResourceUUID(node_templates, id);
            resourceSpecification.put("modelCustomizationId", toscaInfosFromResourceId.get("customizationUUID"));
            resourceSpecification.put("modelCustomizationName", toscaInfosFromResourceId.get("name"));

        }
    }

    private List<LinkedHashMap> buildServiceSpecCharacteristicsValues(LinkedHashMap parameter, String parameterType) {
        List<LinkedHashMap> serviceSpecCharacteristicValues = new ArrayList<>();
        if (!"map".equalsIgnoreCase(parameterType) && !"list".equalsIgnoreCase(parameterType)) {
            LOGGER.debug("get tosca infos for serviceSpecCharacteristicValues of type map or string : " + parameter);
            Object aDefault = parameter.get("default");
            if (parameter.get("entry_schema") != null) {
                ArrayList entry_schema = (ArrayList) parameter.get("entry_schema");
                if (CollectionUtils.isNotEmpty(entry_schema)) {
                    buildCharacteristicValuesFormShema(parameterType, serviceSpecCharacteristicValues, aDefault,
                            entry_schema);
                }
            }
        }
        return serviceSpecCharacteristicValues;
    }

    private void buildCharacteristicValuesFormShema(String parameterType,
            List<LinkedHashMap> serviceSpecCharacteristicValues, Object aDefault, ArrayList entry_schema) {
        LinkedHashMap constraints = (LinkedHashMap) entry_schema.get(0);
        if (constraints != null) {
            ArrayList constraintsList = (ArrayList) constraints.get("constraints");
            if (CollectionUtils.isNotEmpty(constraintsList)) {
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


    private LinkedHashMap getToscaInfosFromResourceUUID(LinkedHashMap node_templates, String name) {
        for (Object nodeTemplateObject : node_templates.values()) {
            LinkedHashMap nodeTemplate = (LinkedHashMap) nodeTemplateObject;
            LinkedHashMap metadata = (LinkedHashMap) nodeTemplate.get("metadata");
            String metadataUUID = (String) metadata.get("UUID");
            String metadataType = (String) metadata.get("type");
            if ("VF".equalsIgnoreCase(metadataType) && name.equalsIgnoreCase(metadataUUID)) {
                return metadata;
            }
        }
        return null;
    }


    public LinkedHashMap getToscaInfos(LinkedHashMap sdcResponse) {
        String toscaModelUrl = (String) sdcResponse.get("toscaModelURL");
        String serviceId = (String) sdcResponse.get("uuid");
        File toscaFile = sdcClient.callGetWithAttachment(toscaModelUrl);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String tempFolderName = serviceId + timestamp;
        File folderTemp = null;
        LinkedHashMap topology_template = null;
        try {
            unZipArchive(toscaFile.getName(), tempFolderName);
            folderTemp = new File(tempFolderName);
            LOGGER.debug("temp folder for tosca files : " + folderTemp.getName());

            LinkedHashMap toscaMetaFileHashMap = parseToscaFile(tempFolderName + "/TOSCA-Metadata/TOSCA.meta");
            if (toscaMetaFileHashMap.get("Entry-Definitions") == null) {
                throw new NullPointerException("no Entry-Definitions node in TOSCA.meta");
            }
            String toscaFilePath = (String) toscaMetaFileHashMap.get("Entry-Definitions");
            LinkedHashMap toscaFileHashMap = parseToscaFile(tempFolderName + "/" + toscaFilePath);

            if (toscaFileHashMap.get("topology_template") == null) {
                throw new NullPointerException("no topology_template node in tosca file");
            }
            topology_template = (LinkedHashMap) toscaFileHashMap.get("topology_template");

        } catch (NullPointerException e) {
            LOGGER.error("unable to parse tosca file for id : " + serviceId + ", " + e.getMessage());
            return null;
        } finally {
            try {
                LOGGER.debug("deleting temp folder for tosca files : " + folderTemp.getName());
                FileUtils.deleteDirectory(folderTemp);
                LOGGER.debug("deleting tosca archive : " + toscaFile.getName());
                FileUtils.forceDelete(toscaFile);
                return topology_template;
            } catch (IOException e) {
                LOGGER.error("unable to delete temp directory tosca file for id : " + serviceId);
                return null;

            }
        }
    }


    private LinkedHashMap parseToscaFile(String fileName) {

        File toscaFile = new File(fileName);
        if (toscaFile == null) {
            throw new TechnicalException("unable to find  file : " + fileName);
        }
        try {
            return (LinkedHashMap) mapper.readValue(toscaFile, Object.class);
        } catch (IOException e) {
            LOGGER.error("unable to parse tosca file : " + fileName);
            LOGGER.error(e.getMessage());
            throw new TechnicalException("Unable to parse tosca file : " + fileName);

        } catch (NullPointerException e) {
            LOGGER.error("unable to find tosca file : " + fileName);
            LOGGER.error(e.getMessage());
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

                        fos.close();
                    }
                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();
            }

            LOGGER.debug("Done");

        } catch (IOException ex) {
            LOGGER.error("Error while unzipping ToscaModel archive from ONAP : " + ex.getMessage());
            throw new TechnicalException("Error while unzipping ToscaModel archive from ONAP");
        }
    }


}
