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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ServiceSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same specification, these services would therefore share the same set of characteristics.
 */
@ApiModel(description = "ServiceSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same specification, these services would therefore share the same set of characteristics.")
public class ServiceSpecificationRequest {

  @NotEmpty(message = "is missing in the request!")
  private String name = null;

  @NotEmpty(message = "is missing in the request!")
  private String description = null;

  private String type = "ONAPservice";

  private String schemaLocation = null;

  private String baseType = null;

  private String toscaModelURL = null;

  private String toscaResourceName = null;

  @NotEmpty(message = "is missing in the request!")
  private String category = null;

  private String subcategory = null;

  private String version = null;

  private LifecycleStatusValues lifecycleStatus = null;

  private TargetServiceSchemaRef targetServiceSchema = null;

  private List<Attachment> attachment = null;

  @NotEmpty(message = "is missing in the request!")
  @Valid
  private List<RelatedPartyRef> relatedParty = null;

  private List<ResourceSpecificationRef> resourceSpecification = null;

  private List<ServiceSpecCharacteristicRequest> serviceSpecCharacteristic = null;

   /**
   * Name of the service specification
   * @return name
  **/
  @ApiModelProperty(value = "Name of the service specification")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }



   /**
   * A narrative that explains in detail what the service specification
   * @return description
  **/
  @ApiModelProperty(value = "A narrative that explains in detail what the service specification")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

   /**
   * This attribute allows to dynamically extends TMF class. Valued with &#39;ONAPservice&#39;. We use this feature to add following attributes: toscaModelURL toscaResourceName category (1) subcategory (1) distributionStatus
   * @return type
  **/
  @ApiModelProperty(value = "This attribute allows to dynamically extends TMF class. Valued with 'ONAPservice'. We use this feature to add following attributes: toscaModelURL toscaResourceName category (1) subcategory (1) distributionStatus")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

   /**
   * Not used
   * @return schemaLocation
  **/
  @ApiModelProperty(value = "Not used")
  public String getSchemaLocation() {
    return schemaLocation;
  }

  public void setSchemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }



   /**
   * Not used
   * @return baseType
  **/
  @ApiModelProperty(value = "Not used")
  public String getBaseType() {
    return baseType;
  }

  public void setBaseType(String baseType) {
    this.baseType = baseType;
  }



   /**
   * Additional attribute (not in the TMF API) - extended through @type - toscaModelURL
   * @return toscaModelURL
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - toscaModelURL")
  public String getToscaModelURL() {
    return toscaModelURL;
  }

  public void setToscaModelURL(String toscaModelURL) {
    this.toscaModelURL = toscaModelURL;
  }

   /**
   * Additional attribute (not in the TMF API) - extended through @type - toscaResourceName
   * @return toscaResourceName
  **/
  @ApiModelProperty(value = "Additional attribute (not in the TMF API) - extended through @type - toscaResourceName")
  public String getToscaResourceName() {
    return toscaResourceName;
  }

  public void setToscaResourceName(String toscaResourceName) {
    this.toscaResourceName = toscaResourceName;
  }

   /**
   * Additional attribute - extended through @type - category Please note that this attribute is managed in TMF - in future release we&#39;ll introduce category resource
   * @return category
  **/
  @ApiModelProperty(value = "Additional attribute - extended through @type - category Please note that this attribute is managed in TMF - in future release we'll introduce category resource")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

   /**
   * Additional attribute - extended through @type - category Please note that this attribute is managed in TMF - in future release we&#39;ll introduce category resource
   * @return subcategory
  **/
  @ApiModelProperty(value = "Additional attribute - extended through @type - category Please note that this attribute is managed in TMF - in future release we'll introduce category resource")
  public String getSubcategory() {
    return subcategory;
  }

  public void setSubcategory(String subcategory) {
    this.subcategory = subcategory;
  }

    /**
   * Service specification version
   * @return version
  **/
  @ApiModelProperty(value = "Service specification version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

   /**
   * Get lifecycleStatus
   * @return lifecycleStatus
  **/
  @ApiModelProperty(value = "")
  public LifecycleStatusValues getLifecycleStatus() {
    return lifecycleStatus;
  }

  public void setLifecycleStatus(LifecycleStatusValues lifecycleStatus) {
    this.lifecycleStatus = lifecycleStatus;
  }

   /**
   * Get targetServiceSchema
   * @return targetServiceSchema
  **/
  @ApiModelProperty(value = "")
  public TargetServiceSchemaRef getTargetServiceSchema() {
    return targetServiceSchema;
  }

  public void setTargetServiceSchema(TargetServiceSchemaRef targetServiceSchema) {
    this.targetServiceSchema = targetServiceSchema;
  }

  public ServiceSpecificationRequest addAttachmentItem(Attachment attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<Attachment>();
    }
    this.attachment.add(attachmentItem);
    return this;
  }

   /**
   * Get attachment
   * @return attachment
  **/
  @ApiModelProperty(value = "")
  public List<Attachment> getAttachment() {
    return attachment;
  }

  public void setAttachment(List<Attachment> attachment) {
    this.attachment = attachment;
  }

  public ServiceSpecificationRequest addRelatedPartyItem(RelatedPartyRef relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<RelatedPartyRef>();
    }
    this.relatedParty.add(relatedPartyItem);
    return this;
  }

   /**
   * Get relatedParty
   * @return relatedParty
  **/
  @ApiModelProperty(value = "")
  public List<RelatedPartyRef> getRelatedParty() {
    return relatedParty;
  }

  public void setRelatedParty(List<RelatedPartyRef> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public ServiceSpecificationRequest addResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (this.resourceSpecification == null) {
      this.resourceSpecification = new ArrayList<ResourceSpecificationRef>();
    }
    this.resourceSpecification.add(resourceSpecificationItem);
    return this;
  }

   /**
   * Get resourceSpecification
   * @return resourceSpecification
  **/
  @ApiModelProperty(value = "")
  public List<ResourceSpecificationRef> getResourceSpecification() {
    return resourceSpecification;
  }

  public void setResourceSpecification(List<ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
  }

  public ServiceSpecificationRequest addServiceSpecCharacteristicItem(ServiceSpecCharacteristicRequest serviceSpecCharacteristicItem) {
    if (this.serviceSpecCharacteristic == null) {
      this.serviceSpecCharacteristic = new ArrayList<ServiceSpecCharacteristicRequest>();
    }
    this.serviceSpecCharacteristic.add(serviceSpecCharacteristicItem);
    return this;
  }

   /**
   * Get serviceSpecCharacteristic
   * @return serviceSpecCharacteristic
  **/
  @ApiModelProperty(value = "")
  public List<ServiceSpecCharacteristicRequest> getServiceSpecCharacteristic() {
    return serviceSpecCharacteristic;
  }

  public void setServiceSpecCharacteristic(List<ServiceSpecCharacteristicRequest> serviceSpecCharacteristic) {
    this.serviceSpecCharacteristic = serviceSpecCharacteristic;
  }




}

