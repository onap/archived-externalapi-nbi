/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
package org.onap.nbi.apis.status.model;

import java.util.HashSet;
import java.util.Set;
import org.onap.nbi.commons.Resource;

public class ApplicationStatus implements Resource {

    private String name;

    private StatusType status;

    private String version;

    private Set<ApplicationStatus> components = new HashSet<>();

    /**
     * Builds a new {@code ApplicationStatus} with the following attributes :
     *
     * @param name name of the service
     * @param state state of the service ({@code OK} | {@code KO})
     * @param version version of the service ({@code x.y.z})
     */
    public ApplicationStatus(final String name, final StatusType status, final String version) {
        this.name = name;
        this.status = status;
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public StatusType getStatus() {
        return this.status;
    }

    public String getVersion() {
        return this.version;
    }

    public Set<ApplicationStatus> getComponents() {
        return this.components;
    }

    public ApplicationStatus component(final ApplicationStatus componentStatus) {
        this.components.add(componentStatus);
        return this;
    }

    @Override
    public String getId() {
        return null;
    }
}
