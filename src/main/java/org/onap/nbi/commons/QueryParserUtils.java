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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 */
public class QueryParserUtils {

    private QueryParserUtils() {}

    /**
     *
     * @param queryParameters
     * @return
     */
    public static Set<String> getFields(MultiValueMap<String, String> queryParameters) {

        Set<String> fieldSet = new HashSet<>();
        if (queryParameters != null) {
            // search for "all" parameter
            if (queryParameters.containsKey(ReservedKeys.ALL_FIELDS)) {
                queryParameters.remove(ReservedKeys.ALL_FIELDS);
                fieldSet.add(ReservedKeys.ALL_FIELDS);
            }
            // search for "fields" parameters
            List<String> queryParameterField =
                    queryParameters.remove(ReservedKeys.QUERY_KEY_FIELD_ESCAPE + ReservedKeys.QUERY_KEY_FIELD);
            if (queryParameterField == null) {
                queryParameterField = queryParameters.remove(ReservedKeys.QUERY_KEY_FIELD);
            }
            if (queryParameterField != null && !queryParameterField.isEmpty()) {
                String queryParameterValue = queryParameterField.get(0);
                if (queryParameterValue != null) {
                    String[] tokenArray = queryParameterValue.split(",");
                    fieldSet.addAll(Arrays.asList(tokenArray));
                }
            }
        }
        return fieldSet;
    }

    public static MultiValueMap<String, String> popCriteria(MultiValueMap<String, String> queryParameters) {

        Set<Entry<String, List<String>>> entrySet = queryParameters.entrySet();

        MultiValueMap<String, String> criterias = new LinkedMultiValueMap<>();

        entrySet.stream().forEach(entry -> {
            final List<String> tempValues = new ArrayList<>();
            entry.getValue().stream().forEach(value -> tempValues.addAll(Arrays.asList(value.split(","))));
            criterias.put(entry.getKey(), tempValues);
        });

        return criterias;
    }

}
