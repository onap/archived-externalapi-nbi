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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonFilter {

    private final static List<String> SKIPPED_FIELDS = Arrays.asList("internalId");


    public static <R> List<ObjectNode> createNodes(List<R> list, JsonRepresentation jsonRepresentation) {

        Set<R> set;
        if (list == null) {
            set = new LinkedHashSet<>();
        } else {
            set = new LinkedHashSet<>(list);
        }
        return createNodes(set, jsonRepresentation);
    }

    public static <R> List<ObjectNode> createNodes(Collection<R> collection, JsonRepresentation jsonRepresentation) {
        List<ObjectNode> nodeList = new ArrayList<>();
        for (R element : collection) {
            ObjectNode node = createNode(element, jsonRepresentation);
            nodeList.add(node);
        }
        return nodeList;
    }

    public static <R> ObjectNode createNode(R bean, JsonRepresentation jsonRepresentation) {
        ObjectMapper mapper = new ObjectMapper();
        return JacksonFilter.createNode(mapper, bean, jsonRepresentation.getAttributes());
    }

    private static <R> ObjectNode createNode(ObjectMapper mapper, R bean, Set<String> names) {
        // split fieldNames in 2 categories :
        // simpleFields for simple property names with no '.'
        // nestedFields for nested property names with a '.'
        Set<String> simpleFields = new LinkedHashSet<String>();
        MultiValueMap nestedFields = new LinkedMultiValueMap();
        buildFields(names, simpleFields, nestedFields);

        // create a simple node with only one level containing all simples
        // properties
        ObjectNode rootNode = JacksonFilter.createNodeWithSimpleFields(mapper, bean, simpleFields);

        // create nested nodes with deeper levels
        Set<Map.Entry<String, List<String>>> entrySet = nestedFields.entrySet();
        // for each nested value, create recursively a node
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String rootFieldName = entry.getKey();
            // add in current node only if full value is not already present in
            // 1st level
            if (!simpleFields.contains(rootFieldName)) {
                Object nestedBean = BeanUtils.getNestedProperty(bean, rootFieldName);
                // add only non null fields
                if (nestedBean == null) {
                    continue;
                }
                Set<String> nestedFieldNames = new LinkedHashSet<String>(entry.getValue());
                // current node is an array or a list
                if ((nestedBean.getClass().isArray()) || (Collection.class.isAssignableFrom(nestedBean.getClass()))) {
                    handleListNode(mapper, rootNode, rootFieldName, nestedBean, nestedFieldNames);
                } else {
                    // create recursively a node and add it in current root node
                    createNodeRecursively(mapper, rootNode, rootFieldName, nestedBean, nestedFieldNames);
                }
            }
        }
        return rootNode;
    }

    private static void createNodeRecursively(ObjectMapper mapper, ObjectNode rootNode, String rootFieldName,
            Object nestedBean, Set<String> nestedFieldNames) {
        ObjectNode nestedNode = JacksonFilter.createNode(mapper, nestedBean, nestedFieldNames);
        if ((nestedNode != null) && (nestedNode.size() > 0)) {
            rootNode.set(rootFieldName, nestedNode);
        }
    }

    private static void buildFields(Set<String> names, Set<String> simpleFields, MultiValueMap nestedFields) {
        for (String name : names) {
            int index = name.indexOf('.');
            boolean isNestedField = (index > 0) && (index < name.length());
            if (isNestedField) {
                String rootFieldName = name.substring(0, index);
                String subFieldName = name.substring(index + 1);
                nestedFields.add(rootFieldName, subFieldName);
            } else {
                simpleFields.add(name);
            }
        }
    }

    private static void handleListNode(ObjectMapper mapper, ObjectNode rootNode, String rootFieldName,
            Object nestedBean, Set<String> nestedFieldNames) {
        Object[] array = null;
        if ((nestedBean.getClass().isArray())) {
            array = (Object[]) nestedBean;
        } else {
            Collection<?> collection = (Collection<?>) nestedBean;
            array = collection.toArray();
        }
        if (array.length > 0) {
            // create a node for each element in array
            // and add created node in an arrayNode
            Collection<JsonNode> nodes = new LinkedList<JsonNode>();
            for (Object object : array) {
                ObjectNode nestedNode = JacksonFilter.createNode(mapper, object, nestedFieldNames);
                if ((nestedNode != null) && (nestedNode.size() > 0)) {
                    nodes.add(nestedNode);
                }
            }
            ArrayNode arrayNode = mapper.createArrayNode();
            arrayNode.addAll(nodes);
            if (arrayNode.size() > 0) {
                rootNode.set(rootFieldName, arrayNode);
            }
        }
    }

    private static <R> ObjectNode createNodeWithSimpleFields(ObjectMapper mapper, R bean, Set<String> names) {
        ObjectNode node = mapper.createObjectNode();
        for (String name : names) {
            // Prevent getting value of some fields
            if (SKIPPED_FIELDS.contains(name)) {
                continue;
            }

            JacksonFilter.nodePut(node, name, BeanUtils.getNestedProperty(bean, name));
        }
        return node;
    }

    private static void nodePut(ObjectNode node, String name, Object value) {
        if (value instanceof Boolean) {
            node.put(name, (Boolean) value);
        } else if (value instanceof Integer) {
            node.put(name, (Integer) value);
        } else if (value instanceof Long) {
            node.put(name, (Long) value);
        } else if (value instanceof Float) {
            node.put(name, (Float) value);
        } else if (value instanceof Double) {
            node.put(name, (Double) value);
        } else if (value instanceof BigDecimal) {
            node.put(name, (BigDecimal) value);
        } else if (value instanceof String) {
            node.put(name, (String) value);
        } else {
            node.putPOJO(name, value);
        }
    }

}
