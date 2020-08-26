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

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * Party linked to the service catalog. in nbi we retrieve information about last updater of the service in SDC
 */
@ApiModel(description = "Party linked to the service catalog. in nbi we retrieve information about last updater of the service in SDC")

public class RelatedPartyRef {

  @NotEmpty(message = "is missing in the request!")
  private String id = null;

  private String role = null;

  private String name = null;

  /**
   * Unique identifier of the related party. Filled with lastUpdaterUserId
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of the related party. Filled with lastUpdaterUserId")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Role payed by the related party Only role &#39;lastUpdater&#39; is retrieved in Beijing release
   * @return role
  **/
  @ApiModelProperty(value = "Role payed by the related party Only role 'lastUpdater' is retrieved in Beijing release")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Name of the related party - Filled with lastUpdatedFullName
   * @return name
  **/
  @ApiModelProperty(value = "Name of the related party - Filled with lastUpdatedFullName")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }



}

