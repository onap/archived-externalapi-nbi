/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
