package org.onap.nbi.apis.servicecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.nbi.commons.Resource;
import org.springframework.data.annotation.Id;

@ApiModel
 public class SpecificationInputSchema implements Resource {


        @Id
        @JsonProperty("id")
        private String id = null;

        @JsonProperty("schema")
        private String specificationSchemaJson = null;

        @Override
        @JsonProperty("id")
        @ApiModelProperty(required = true, value = "uuid for the specification input schema")
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("schema")
        @ApiModelProperty(required = true, value = "Input schema for the service")
        public String getSpecificationSchemaJson() {
            return specificationSchemaJson;
        }

        public void setSpecificationSchemaJson(String specificationSchemaJson) {
            this.specificationSchemaJson = specificationSchemaJson;
        }
}
