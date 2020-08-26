/*
 * ============LICENSE_START=============================================================================================================
 * Copyright (c) 2020 <rakeshgirijaramesannair>
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
 * An attachment is a file uses to describe the service. In nbi we use attachment to retrieve ONAP artifacts.
 */
@ApiModel(description = "An attachment is a file uses to describe the service. In nbi we use attachment to retrieve ONAP artifacts.")


public class Attachment {

  private String id = null;


  private String name = null;

  private String description = null;

  private String type = "ONAPartifact";

  private String artifactLabel = null;

  private String artifactGroupType = null;

  private String artifactTimeout = null;

  private String artifactChecksum = null;

  private String artifactVersion = null;

  private String generatedFromUUID = null;

  private String url = null;

  private String mimeType = null;

  /**
   * Unique identifier of the attachment - filled with artifactUUID.
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of the attachment - filled with artifactUUID.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Name of the attachment - filled with artifactName
   * @return name
  **/
  @ApiModelProperty(value = "Name of the attachment - filled with artifactName")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Description of the attachment - filled with artifactDescription
   * @return description
  **/
  @ApiModelProperty(value = "Description of the attachment - filled with artifactDescription")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * This attribute allows to dynamically extends TMF class. Valued with &#39;ONAPartifact&#39;. We used this features to add following attributes:  artifactLabel artifactGroupType artifactTimeout artifactChecksum artifactVersion generatedFromUUID
   * @return type
  **/
  @ApiModelProperty(value = "This attribute allows to dynamically extends TMF class. Valued with 'ONAPartifact'. We used this features to add following attributes:  artifactLabel artifactGroupType artifactTimeout artifactChecksum artifactVersion generatedFromUUID")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - artifactLabel
   * @return artifactLabel
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - artifactLabel")
  public String getArtifactLabel() {
    return artifactLabel;
  }

  public void setArtifactLabel(String artifactLabel) {
    this.artifactLabel = artifactLabel;
  }

  /**
   * Additional attribute (not in the TMF API) - extended through @type - artifactGroupType
   * @return artifactGroupType
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - artifactGroupType")
  public String getArtifactGroupType() {
    return artifactGroupType;
  }

  public void setArtifactGroupType(String artifactGroupType) {
    this.artifactGroupType = artifactGroupType;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - artifactTimeout
   * @return artifactTimeout
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - artifactTimeout")
  public String getArtifactTimeout() {
    return artifactTimeout;
  }

  public void setArtifactTimeout(String artifactTimeout) {
    this.artifactTimeout = artifactTimeout;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - artifactChecksum
   * @return artifactChecksum
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - artifactChecksum")
  public String getArtifactChecksum() {
    return artifactChecksum;
  }

  public void setArtifactChecksum(String artifactChecksum) {
    this.artifactChecksum = artifactChecksum;
  }

  /**
   * Additional attribute (not in the TMF API) - extended through @type - artifactVersion
   * @return artifactVersion
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - artifactVersion")
  public String getArtifactVersion() {
    return artifactVersion;
  }

  public void setArtifactVersion(String artifactVersion) {
    this.artifactVersion = artifactVersion;
  }


   /**
   * Additional attribute (not in the TMF API) - extended through @type - generatedFromUUID
   * @return generatedFromUUID
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - generatedFromUUID")
  public String getGeneratedFromUUID() {
    return generatedFromUUID;
  }

  public void setGeneratedFromUUID(String generatedFromUUID) {
    this.generatedFromUUID = generatedFromUUID;
  }

  /**
   * Uniform Resource Locator, is a web page address - filled with artifactURL
   * @return url
  **/
  @ApiModelProperty(value = "Uniform Resource Locator, is a web page address - filled with artifactURL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Filled with artifactType
   * @return mimeType
  **/
  @ApiModelProperty(value = "Filled with artifactType")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }




}

