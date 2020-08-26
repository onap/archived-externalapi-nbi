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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A characteristic quality or distinctive feature of a ServiceSpecification.
 */
@ApiModel(description = "A characteristic quality or distinctive feature of a ServiceSpecification.")

public class ServiceSpecCharacteristicRequest {
  private String name = null;

  private String description = null;

  private String valueType = null;

  private String type = null;

  private String schemaLocation = null;

  private Boolean required = true;

  private List<ServiceSpecCharacteristicValueRequest> serviceSpecCharacteristicValue = null;

   /**
   * Name of the characteristic
   * @return name
  **/
  @ApiModelProperty(value = "Name of the characteristic")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

   /**
   * A narrative that explains in detail what the characteristic is
   * @return description
  **/
  @ApiModelProperty(value = "A narrative that explains in detail what the characteristic is")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

   /**
   * A kind of value that the characteristic can take on, from Dublin from Dublin we use the object type to describe service characteristic values
   * @return valueType
  **/
  @ApiModelProperty(value = "A kind of value that the characteristic can take on, from Dublin from Dublin we use the object type to describe service characteristic values")
  public String getValueType() {
    return valueType;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

   /**
   * This attribute allows to dynamically extends TMF class. Valued with: &#39;ONAPserviceCharacteristic&#39;. We do not used this feature in nbi
   * @return type
  **/
  @ApiModelProperty(value = "This attribute allows to dynamically extends TMF class. Valued with: 'ONAPserviceCharacteristic'. We do not used this feature in nbi")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

   /**
   * An url pointing to type description - we do not use it
   * @return schemaLocation
  **/
  @ApiModelProperty(value = "An url pointing to type description - we do not use it")
  public String getSchemaLocation() {
    return schemaLocation;
  }

  public void setSchemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }

   /**
   * A parameter to define if the characteristic is mandatory
   * @return required
  **/
  @ApiModelProperty(value = "A parameter to define if the characteristic is mandatory")
  public Boolean isRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public ServiceSpecCharacteristicRequest addServiceSpecCharacteristicValueItem(ServiceSpecCharacteristicValueRequest serviceSpecCharacteristicValueItem) {
    if (this.serviceSpecCharacteristicValue == null) {
      this.serviceSpecCharacteristicValue = new ArrayList<ServiceSpecCharacteristicValueRequest>();
    }
    this.serviceSpecCharacteristicValue.add(serviceSpecCharacteristicValueItem);
    return this;
  }

   /**
   * Get serviceSpecCharacteristicValue
   * @return serviceSpecCharacteristicValue
  **/
  @ApiModelProperty(value = "")
  public List<ServiceSpecCharacteristicValueRequest> getServiceSpecCharacteristicValue() {
    return serviceSpecCharacteristicValue;
  }

  public void setServiceSpecCharacteristicValue(List<ServiceSpecCharacteristicValueRequest> serviceSpecCharacteristicValue) {
    this.serviceSpecCharacteristicValue = serviceSpecCharacteristicValue;
  }
}

