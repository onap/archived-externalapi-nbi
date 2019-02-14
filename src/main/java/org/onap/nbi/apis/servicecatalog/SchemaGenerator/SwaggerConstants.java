/**
 * Copyright (c) 2018 Huawei
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.nbi.apis.servicecatalog.SchemaGenerator;

import java.util.ArrayList;
import java.util.List;

public class SwaggerConstants {
	  
	  public final static String RESPONSE_HEADER_TRANSACTION_ID = "X-ONAP-RequestID";
	  public final static String RESPONSE_HEADER_MINOR_VERSION = "X-MinorVersion";
	  public final static String RESPONSE_HEADER_PATCH_VERSION = "X-PatchVersion";
	  public final static String RESPONSE_HEADER_LATEST_VERSION = "X-LatestVersion";
	  public final static String STATUS_SUCCESS = "success";
	  public final static String STATUS_PROCESSING = "processing";
	  public final static String STATUS_FAILURE = "failure";
	  public final static String TYPE_DEFAULT = "default";
	  public final static String DATA_TYPE_STRING = "string";
	  public final static String DATA_TYPE_INTEGER = "integer";
	  public final static String DATA_TYPE_FLOAT = "float";
	  public final static String DATA_TYPE_DOUBLE = "double";
	  public final static String DATA_TYPE_BOOLEAN = "boolean";
	  public final static String DATA_TYPE_TIMESTAMP = "timestamp";
	  public final static String DATA_TYPE_NULL = "null";
	  public final static String DATA_TYPE_LIST = "list";
	  public final static String DATA_TYPE_MAP = "map";
	  public final static String USER_SYSTEM = "System";
	  public final static String PATH_DIVIDER = "/";
	  public final static String PATH_SERVICE_TEMPLATE = "service_template";
	  public final static String PATH_TOPOLOGY_TEMPLATE = "topology_template";
	  public final static String PATH_METADATA = "metadata";
	  public final static String PATH_NODE_TYPES = "node_types";
	  public final static String PATH_POLICY_TYPES = "policy_types";
	  public final static String PATH_RELATIONSHIP_TYPES = "relationship_types";
	  public final static String PATH_ARTIFACT_TYPES = "artifact_types";
	  public final static String PATH_DATA_TYPES = "data_types";
	  public final static String PATH_INPUTS = "inputs";
	  public final static String PATH_NODE_WORKFLOWS = "workflows";
	  public final static String PATH_NODE_TEMPLATES = "node_templates";
	  public final static String PATH_CAPABILITIES = "capabilities";
	  public final static String PATH_REQUIREMENTS = "requirements";
	  public final static String PATH_INTERFACES = "interfaces";
	  public final static String PATH_OPERATIONS = "operations";
	  public final static String PATH_OUTPUTS = "outputs";
	  public final static String PATH_PROPERTIES = "properties";
	  public final static String PATH_ATTRIBUTES = "attributes";
	  public final static String PATH_ARTIFACTS = "artifacts";
	  public final static String MODEL_DIR_MODEL_TYPE = "definition-type";
	  public final static String MODEL_DEFINITION_TYPE_NODE_TYPE = "node_type";
	  public final static String MODEL_DEFINITION_TYPE_ARTIFACT_TYPE = "artifact_type";
	  public final static String MODEL_DEFINITION_TYPE_CAPABILITY_TYPE = "capability_type";
	  public final static String MODEL_DEFINITION_TYPE_RELATIONSHIP_TYPE = "relationship_type";
	  public final static String MODEL_DEFINITION_TYPE_DATA_TYPE = "data_type";
	  public final static String MODEL_TYPE_DATATYPES_ROOT = "tosca.datatypes.Root";
	  public final static String MODEL_TYPE_NODES_ROOT = "tosca.nodes.Root";
	  public final static String MODEL_TYPE_GROUPS_ROOT = "tosca.groups.Root";
	  public final static String MODEL_TYPE_RELATIONSHIPS_ROOT = "tosca.relationships.Root";
	  public final static String MODEL_TYPE_ARTIFACTS_ROOT = "tosca.artifacts.Root";
	  public final static String MODEL_TYPE_CAPABILITIES_ROOT = "tosca.capabilities.Root";
	  public final static String MODEL_TYPE_INTERFACES_ROOT = "tosca.interfaces.Root";
	  public final static String MODEL_TYPE_RELATIONSHIPS_DEPENDS_ON = "tosca.relationships.DependsOn";
	  public final static String MODEL_TYPE_RELATIONSHIPS_HOSTED_ON = "tosca.relationships.HostedOn";
	  public final static String MODEL_TYPE_RELATIONSHIPS_CONNECTS_TO = "tosca.relationships.ConnectsTo";
	  public final static String MODEL_TYPE_RELATIONSHIPS_ATTACH_TO = "tosca.relationships.AttachesTo";
	  public final static String MODEL_TYPE_RELATIONSHIPS_ROUTES_TO = "tosca.relationships.RoutesTo";
	  public final static String MODEL_TYPE_NODE_DG = "tosca.nodes.DG";
	  public final static String MODEL_TYPE_NODE_COMPONENT = "tosca.nodes.Component";
	  public final static String MODEL_TYPE_NODE_VNF = "tosca.nodes.Vnf";
	  public final static String MODEL_TYPE_NODE_ARTIFACT = "tosca.nodes.Artifact";
	  public final static String MODEL_TYPE_NODE_RESOURCE_SOURCE = "tosca.nodes.ResourceSource";
	  public final static String MODEL_TYPE_NODES_COMPONENT_JAVA = "tosca.nodes.component.Java";
	  public final static String MODEL_TYPE_NODES_COMPONENT_BUNDLE = "tosca.nodes.component.Bundle";
	  public final static String MODEL_TYPE_NODES_COMPONENT_SCRIPT = "tosca.nodes.component.Script";
	  public final static String MODEL_TYPE_NODES_COMPONENT_PYTHON = "tosca.nodes.component.Python";
	  public final static String MODEL_TYPE_NODES_COMPONENT_JYTHON = "tosca.nodes.component.Jython";
	  public final static String MODEL_TYPE_NODES_COMPONENT_JAVA_SCRIPT = "tosca.nodes.component.JavaScript";
	  public final static String MODEL_TYPE_ARTIFACT_TYPE_IMPLEMENTATION = "tosca.artifacts.Implementation";
	  public final static String MODEL_TYPE_DATA_TYPE_DYNAMIC = "tosca.datatypes.Dynamic";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_NODE = "tosca.capabilities.Node";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_COMPUTE = "tosca.capabilities.Compute";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_NETWORK = "tosca.capabilities.Network";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_STORAGE = "tosca.capabilities.Storage";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_ENDPOINT = "tosca.capabilities.Endpoint";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_ENDPOINT_PUBLIC = "tosca.capabilities.Endpoint.Public";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_ENDPOINT_ADMIN = "tosca.capabilities.Endpoint.Admin";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_ENDPOINT_DATABASE = "tosca.capabilities.Endpoint.Database";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_ATTACHMENT = "tosca.capabilities.Attachment";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_OPERATION_SYSTEM = "tosca.capabilities.OperatingSystem";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_BINDABLE = "tosca.capabilities.network.Bindable";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_CONTENT = "tosca.capabilities.Content";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_MAPPING = "tosca.capabilities.Mapping";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_NETCONF = "tosca.capabilities.Netconf";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_SSH = "tosca.capabilities.Ssh";
	  public final static String MODEL_TYPE_CAPABILITY_TYPE_SFTP = "tosca.capabilities.Sftp";
	  public final static String EXPRESSION_GET_INPUT = "get_input";
	  public final static String EXPRESSION_GET_ATTRIBUTE = "get_attribute";
	  public final static String EXPRESSION_GET_ARTIFACT = "get_artifact";
	  public final static String EXPRESSION_GET_PROPERTY = "get_property";
	  public final static String EXPRESSION_GET_OPERATION_OUTPUT = "get_operation_output";
	  public final static String EXPRESSION_GET_NODE_OF_TYPE = "get_nodes_of_type";
	  public final static String PROPERTY_BLUEPRINT_PROCESS_ID = "blueprint-process-id";
	  public final static String PROPERTY_BLUEPRINT_BASE_PATH = "blueprint-basePath";
	  public final static String PROPERTY_BLUEPRINT_RUNTIME = "blueprint-runtime";
	  public final static String PROPERTY_BLUEPRINT_INPUTS_DATA = "blueprint-inputs-data";
	  public final static String PROPERTY_BLUEPRINT_CONTEXT = "blueprint-context";
	  public final static String PROPERTY_BLUEPRINT_NAME = "template_name";
	  public final static String PROPERTY_BLUEPRINT_VERSION = "template_version";
	  public final static String TOSCA_METADATA_DIR = "TOSCA-Metadata";
	  public final static String TOSCA_METADATA_ENTRY_DEFINITION_FILE = "TOSCA-Metadata/TOSCA.meta";
	  public final static String TOSCA_DEFINITIONS_DIR = "Definitions";
	  public final static String TOSCA_PLANS_DIR = "Plans";
	  public final static String TOSCA_SCRIPTS_DIR = "Scripts";
	  public final static String TOSCA_MAPPINGS_DIR = "Mappings";
	  public final static String TOSCA_TEMPLATES_DIR = "Templates";
	  public final static String METADATA_USER_GROUPS = "user-groups";
	  public final static String METADATA_TEMPLATE_NAME = "template_name";
	  public final static String METADATA_TEMPLATE_VERSION = "template_version";
	  public final static String METADATA_TEMPLATE_AUTHOR = "template_author";
	  public final static String METADATA_TEMPLATE_TAGS = "template_tags";
	  public final static String METADATA_WORKFLOW_NAME = "workflow_name";
	  public final static String PAYLOAD_DATA = "payload-data";
	  public final static String PROPERTY_CURRENT_STEP = "current-step";
	  public final static String PROPERTY_CURRENT_NODE_TEMPLATE = "current-node-template";
	  public final static String PROPERTY_CURRENT_INTERFACE = "current-interface";
	  public final static String PROPERTY_CURRENT_OPERATION = "current-operation";
	  public final static String PROPERTY_CURRENT_IMPLEMENTATION = "current-implementation";
	  public final static String PROPERTY_EXECUTION_REQUEST = "execution-request";
	  public final static String OPERATION_PROCESS = "process";
	  public final static String OPERATION_PREPARE = "prepare";
	  public final static String BLUEPRINT_RETRIEVE_TYPE_DB = "db";
	  public final static String BLUEPRINT_RETRIEVE_TYPE_FILE = "file";
	  public final static String BLUEPRINT_RETRIEVE_TYPE_REPO = "repo";

	  public  static List<String> validPrimitiveTypes() {
	        List<String> validTypes = new ArrayList<String>();
	        validTypes.add(DATA_TYPE_STRING);
	        validTypes.add(DATA_TYPE_INTEGER);
	        validTypes.add(DATA_TYPE_FLOAT);
	        validTypes.add(DATA_TYPE_DOUBLE);
	        validTypes.add(DATA_TYPE_BOOLEAN);
	        validTypes.add(DATA_TYPE_TIMESTAMP);
	        validTypes.add(DATA_TYPE_NULL);
	        return validTypes;
	    }
}
