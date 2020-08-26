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
 * A json schema of the service specification input  characteristic values
 */
@ApiModel(description = "A json schema of the service specification input  characteristic values")

public class ServiceSpecCharacteristicValueRequest {
  private String valueType = null;

  private Boolean isDefault = null;

  private String value = null;

   /**
   * This attribute describes the type of value
   * @return valueType
  **/
  @ApiModelProperty(value = "This attribute describes the type of value")
  public String getValueType() {
    return valueType;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

   /**
   * To describe whether the attribute is to be included by default or not
   * @return isDefault
  **/
  @ApiModelProperty(value = "To describe whether the attribute is to be included by default or not")
  public Boolean isIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }


   /**
   * Value of the attribute
   * @return value
  **/
  @ApiModelProperty(value = "Value of the attribute")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }



}

