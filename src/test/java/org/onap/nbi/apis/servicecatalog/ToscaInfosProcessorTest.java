/**
 *     Copyright (c) 2018 Orange
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.onap.nbi.apis.servicecatalog;

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
        List<LinkedHashMap> resources= new ArrayList<>();
        LinkedHashMap resource1= new LinkedHashMap();
        resource1.put("id","e2b12ac6-cbb6-4517-9c58-b846d1f68caf");
        resources.add(resource1);
        LinkedHashMap toscaFile = parseToscaFile(file.getPath());
        LinkedHashMap response = new LinkedHashMap();
        response.put("resourceSpecification",resources);
        toscaInfosProcessor.buildResponseWithToscaInfos((LinkedHashMap)toscaFile.get("topology_template"),response);

        resources = (List<LinkedHashMap>)response.get("resourceSpecification");
        assertNull(resources.get(0).get("modelCustomizationId"));
        assertNull(resources.get(0).get("modelCustomizationName"));

    }



}