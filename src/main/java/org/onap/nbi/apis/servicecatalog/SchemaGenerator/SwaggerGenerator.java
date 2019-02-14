/*
 *  Copyright © 2017-2018 AT&T Intellectual Property.
 *  Modifications Copyright © 2018 IBM.
 *  Modifications Copyright © 2018 Huawei.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.onap.nbi.apis.servicecatalog.SchemaGenerator;

import io.swagger.models.*;
import io.swagger.util.Json;
import io.swagger.models.properties.*;

import org.apache.commons.lang3.BooleanUtils;

import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.toscaparser.api.parameters.Input;

import java.util.*;

/**
 * SwaggerGenerator.java Purpose: Provide Service to generate service template input schema definition and Sample Json
 * generation.
 *
 * @author Brinda Santh
 * @version 1.0
 */

public class SwaggerGenerator {

	private ISdcCsarHelper csar;
    public static final String INPUTS="inputs";

    /**
     * This is a SwaggerGenerator constructor
     */
    public SwaggerGenerator(ISdcCsarHelper csar) {

        this.csar = csar;
    }

    /**
     * This is a generateSwagger
     *
     * @return String
     */
    public Swagger generateSwagger() {
        
        Swagger swagger = new Swagger();//.info(getInfo());

        //swagger.setPaths(getPaths());
        swagger.setDefinitions(getDefinition());

        return swagger;
    }

    /*private Info getInfo() {
        Info info = new Info();
        Contact contact = new Contact();
        contact.setName(serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_AUTHOR));
        info.setContact(contact);
        info.setTitle(serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_NAME));
        info.setDescription(serviceTemplate.getDescription());
        info.setVersion(serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_VERSION));
        return info;
    }*/

    /*private Map<String, Path> getPaths() {
        Map<String, Path> paths = new HashMap<>();
        Path path = new Path();
        Operation post = new Operation();
        post.setOperationId("configure");
        post.setConsumes(Arrays.asList("application/json", "application/xml"));
        post.setProduces(Arrays.asList("application/json", "application/xml"));
        List<Parameter> parameters = new ArrayList<>();
        Parameter in = new BodyParameter().schema(new RefModel("#/definitions/inputs"));
        in.setRequired(true);
        in.setName(INPUTS);
        parameters.add(in);
        post.setParameters(parameters);

        Map<String, Response> responses = new HashMap<>();
        Response response = new Response().description("Success");
        responses.put("200", response);

        Response failureResponse = new Response().description("Failure");
        responses.put("400", failureResponse);
        post.setResponses(responses);

        path.setPost(post);
        paths.put("/operations/config-selfservice-api:configure", path);
        return paths;
    }*/

    private Map<String, Model> getDefinition() {
        Map<String, Model> models = new HashMap<>();

        ModelImpl inputmodel = new ModelImpl();
        inputmodel.setTitle(INPUTS);               
        
        csar.getServiceInputs().forEach((input) -> {
            Property defProperty = getProperty(input);
            inputmodel.property(input.getName(), defProperty);
        });

        models.put(INPUTS, inputmodel);   

        return models;

    }

    private Property getProperty(Input input) {    	    
    	
        Property defProperty = null;

        if (SwaggerConstants.validPrimitiveTypes().contains(input.getType())) {
            if (SwaggerConstants.DATA_TYPE_BOOLEAN.equals(input.getType())) {
                defProperty = new BooleanProperty();
            } else if (SwaggerConstants.DATA_TYPE_INTEGER.equals(input.getType())) {
                StringProperty stringProperty = new StringProperty();
                stringProperty.setType("integer");
                defProperty = stringProperty;
            } else if (SwaggerConstants.DATA_TYPE_FLOAT.equals(input.getType())) {
                StringProperty stringProperty = new StringProperty();
                stringProperty.setFormat("float");
                defProperty = stringProperty;
            } else if (SwaggerConstants.DATA_TYPE_TIMESTAMP.equals(input.getType())) {
                DateTimeProperty dateTimeProperty = new DateTimeProperty();
                dateTimeProperty.setFormat("date-time");
                defProperty = dateTimeProperty;
            } else {
                defProperty = new StringProperty();
            }
        } 
        else {
            defProperty = new RefProperty("#/definitions/" + input.getType());
        }
        defProperty.setName(input.getName());
        if (input.getDefault() != null) {
            defProperty.setDefault(String.valueOf(input.getDefault()));
        }

        defProperty.setRequired(BooleanUtils.isTrue(input.isRequired()));
        defProperty.setDescription(input.getDescription());
        return defProperty;        
    }


}
