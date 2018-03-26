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

        MultiValueMap<String, String> criterias = new LinkedMultiValueMap<String, String>();

        entrySet.stream().forEach(entry -> {
            final List<String> tempValues = new ArrayList<String>();
            entry.getValue().stream().forEach(value -> tempValues.addAll(Arrays.asList(value.split(","))));
            criterias.put(entry.getKey(), tempValues);
        });

        return criterias;
    }

}
