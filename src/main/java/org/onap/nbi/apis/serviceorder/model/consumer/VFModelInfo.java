/**
 *     Copyright (c) 2020 TechMahindra
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
package org.onap.nbi.apis.serviceorder.model.consumer;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "modelName", "modelUuid", "modelInvariantUuid", "modelVersion", "modelCustomizationUuid" })
public class VFModelInfo {

  @JsonProperty("modelName")
  private String modelName;
  @JsonProperty("modelUuid")
  private String modelUuid;
  @JsonProperty("modelInvariantUuid")
  private String modelInvariantUuid;
  @JsonProperty("modelVersion")
  private String modelVersion;
  @JsonProperty("modelCustomizationUuid")
  private String modelCustomizationUuid;

    @JsonProperty("modelName")
    public String getModelName() {
      return modelName;
    }

    @JsonProperty("modelName")
    public void setModelName(String modelName) {
      this.modelName = modelName;
    }

    @JsonProperty("modelUuid")
    public String getModelUuid() {
      return modelUuid;
    }

    @JsonProperty("modelUuid")
    public void setModelUuid(String modelUuid) {
      this.modelUuid = modelUuid;
    }

    @JsonProperty("modelInvariantUuid")
    public String getModelInvariantUuid() {
      return modelInvariantUuid;
    }

    @JsonProperty("modelInvariantUuid")
    public void setModelInvariantUuid(String modelInvariantUuid) {
      this.modelInvariantUuid = modelInvariantUuid;
    }

    @JsonProperty("modelVersion")
    public String getModelVersion() {
      return modelVersion;
    }

    @JsonProperty("modelVersion")
    public void setModelVersion(String modelVersion) {
      this.modelVersion = modelVersion;
    }

    @JsonProperty("modelCustomizationUuid")
    public String getModelCustomizationUuid() {
      return modelCustomizationUuid;
    }

    @JsonProperty("modelCustomizationUuid")
    public void setModelCustomizationUuid(String modelCustomizationUuid) {
      this.modelCustomizationUuid = modelCustomizationUuid;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("modelName", modelName).append("modelUuid", modelUuid)
        .append("modelInvariantUuid", modelInvariantUuid).append("modelVersion", modelVersion)
        .append("modelCustomizationUuid", modelCustomizationUuid).toString();
    }
}