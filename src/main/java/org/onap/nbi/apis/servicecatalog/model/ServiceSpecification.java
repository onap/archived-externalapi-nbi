package org.onap.nbi.apis.servicecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.nbi.commons.Resource;
import org.springframework.data.annotation.Id;

import java.util.Map;

@ApiModel
public class ServiceSpecification implements Resource {

    @Id
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("catalogResponse")
    private Map catalogResponse = null;

    @Override
    @JsonProperty("id")
    @ApiModelProperty(required = true, value = "uuid for the service specification")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("catalogResponse")
    @ApiModelProperty(required = true, value = "catalogResponse for the corresponding service uuid")
    public Map getCatalogResponse() {
        return catalogResponse;
    }

    public void setCatalogResponse(Map catalogResponse) {
        this.catalogResponse = catalogResponse;
    }
}
