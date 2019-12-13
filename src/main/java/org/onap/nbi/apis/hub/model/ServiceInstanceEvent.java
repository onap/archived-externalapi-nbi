/**
 * Copyright (c) 2019 Huawei
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

import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceInstanceEvent {

    @Id
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("href")
    private String href = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("type")
    private String type = "service-instance";

    @JsonProperty("state")
    private String state = null;

    @JsonProperty("relatedParty")
    private RelatedParty relatedParty = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public RelatedParty getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(RelatedParty relatedParty) {
        this.relatedParty = relatedParty;
    }

}
