/*
 * ============LICENSE_START=============================================================================================================
 * Copyright (c) 2020 NikhilMohan
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * ============LICENSE_END===============================================================================================================
 *
 */


package org.onap.nbi.apis.servicecatalog.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * A list of resourceSpec identified to deliver the service. for nbi we retrieve resource information available in service description (through SDC api) bu as well information retrieved in the TOSCA file.
 */
@ApiModel(description = "A list of resourceSpec identified to deliver the service. for nbi we retrieve resource information available in service description (through SDC api) bu as well information retrieved in the TOSCA file.")

public class ResourceSpecificationRef {
  private String id = null;

  private String version = null;

  private String name = null;

  private String type = "ONAPresource";

  private String resourceInstanceName = null;

  private String resourceInvariantUUID = null;

  private String resourceType = null;

  private String modelCustomizationName = null;

  private String modelCustomizationId = null;


   /**
   * Unique identifier of the resource specification - filled with resourceUUID
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of the resource specification - filled with resourceUUID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Version for this resource specification - filled with resourceVersion
   * @return version
  **/
  @ApiModelProperty(value = "Version for this resource specification - filled with resourceVersion")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


   /**
   * Name of the resource specification - filled with resourceName
   * @return name
  **/
  @ApiModelProperty(value = "Name of the resource specification - filled with resourceName")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


   /**
   * This attribute allows to dynamically extends TMF class. Valued with: &#39;ONAPresource&#39;. We used this features to add following attributes: resourceInstanceName resourceInvariantUUID resourceType modelCustomizationName modelCustomizationId
   * @return type
  **/
  @ApiModelProperty(value = "This attribute allows to dynamically extends TMF class. Valued with: 'ONAPresource'. We used this features to add following attributes: resourceInstanceName resourceInvariantUUID resourceType modelCustomizationName modelCustomizationId")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - resourceInstanceName
   * @return resourceInstanceName
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - resourceInstanceName")
  public String getResourceInstanceName() {
    return resourceInstanceName;
  }

  public void setResourceInstanceName(String resourceInstanceName) {
    this.resourceInstanceName = resourceInstanceName;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - resourceInvariantUUID
   * @return resourceInvariantUUID
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - resourceInvariantUUID")
  public String getResourceInvariantUUID() {
    return resourceInvariantUUID;
  }

  public void setResourceInvariantUUID(String resourceInvariantUUID) {
    this.resourceInvariantUUID = resourceInvariantUUID;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - resoucreType
   * @return resourceType
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - resoucreType")
  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  /**
   * Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute name in topology_template/node_template for the resource
   * @return modelCustomizationName
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute name in topology_template/node_template for the resource")
  public String getModelCustomizationName() {
    return modelCustomizationName;
  }

  public void setModelCustomizationName(String modelCustomizationName) {
    this.modelCustomizationName = modelCustomizationName;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute customizationUUID in topology_template/node_template for the resource
   * @return modelCustomizationId
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute customizationUUID in topology_template/node_template for the resource")
  public String getModelCustomizationId() {
    return modelCustomizationId;
  }

  public void setModelCustomizationId(String modelCustomizationId) {
    this.modelCustomizationId = modelCustomizationId;
  }

}

