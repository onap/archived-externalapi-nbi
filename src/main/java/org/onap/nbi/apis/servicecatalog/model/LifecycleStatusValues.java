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


/**
 * Service lifecycle value from ONAP SDC
 *
 */
public enum LifecycleStatusValues {
  
  NOT_CERTIFIED_CHECKOUT("NOT_CERTIFIED_CHECKOUT"),
  
  NOT_CERTIFIED_CHECKIN("NOT_CERTIFIED_CHECKIN"),
  
  READY_FOR_CERTIFICATION("READY_FOR_CERTIFICATION"),
  
  CERTIFICATION_IN_PROGRESS("CERTIFICATION_IN_PROGRESS"),
  
  CERTIFIED("CERTIFIED");

  private String value;

  LifecycleStatusValues(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }




}

