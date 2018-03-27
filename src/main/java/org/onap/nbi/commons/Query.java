package org.onap.nbi.commons;

import org.springframework.util.MultiValueMap;



public class Query {

    private MultiValueMap<String, String> parameters;

    private JsonRepresentation jsonRepresentation;

    public Query(MultiValueMap<String, String> queryParameters) {
        this.parameters = queryParameters;
        this.jsonRepresentation = new JsonRepresentation(queryParameters);
    }

    public MultiValueMap<String, String> getParameters() {
        return parameters;
    }

    public JsonRepresentation getRepresentation() {
        return jsonRepresentation;
    }

}
