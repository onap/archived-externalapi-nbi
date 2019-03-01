/**
 * Copyright (c) 2018 Orange
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
package org.onap.nbi.apis.hub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {

  SERVICE_ORDER_CREATION("ServiceOrderCreationNotification"),

  SERVICE_ORDER_STATE_CHANGE("ServiceOrderStateChangeNotification"),

  SERVICE_ORDER_ITEM_STATE_CHANGE("ServiceOrderItemStateChangeNotification"),

  SERVICE_CREATION("ServiceCreationNotification"),

  SERVICE_ATTRIBUTE_VALUE_CHANGE("ServiceAttributeValueChangeNotification"),

  SERVICE_REMOVE("ServiceRemoveNotification");

  private String value;

  EventType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EventType fromValue(String text) {
    for (EventType b : EventType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  @JsonValue
  public String value() {
    return this.value;
  }

}
