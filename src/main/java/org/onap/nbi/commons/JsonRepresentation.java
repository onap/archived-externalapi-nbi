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

package org.onap.nbi.commons;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.util.MultiValueMap;

public class JsonRepresentation {

    private Set<String> attributes = new LinkedHashSet<>();

    public JsonRepresentation() {
    }

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
