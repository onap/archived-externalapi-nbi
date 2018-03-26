package org.onap.nbi.commons;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.util.MultiValueMap;

public class JsonRepresentation {

    private Set<String> attributes = new LinkedHashSet<>();

    public JsonRepresentation() {}

    public JsonRepresentation(MultiValueMap<String, String> queryParameters) {
        this.attributes = QueryParserUtils.getFields(queryParameters);
    }

    public JsonRepresentation(Set<String> attributes) {
        this.attributes.addAll(attributes);
    }

    public JsonRepresentation add(String attributePath) {
        this.attributes.add(attributePath);
        return this;
    }

    public JsonRepresentation add(JsonRepresentation jsonRepresentation) {
        this.attributes.addAll(jsonRepresentation.getAttributes());
        return this;
    }

    public Set<String> getAttributes() {
        return attributes;
    }

}
