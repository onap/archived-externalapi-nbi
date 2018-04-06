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
package org.onap.nbi.apis.serviceorder.model.consumer;

public class RequestInfo {

    private String instanceName;

    private String productFamilyId;

    private String source;

    private boolean suppressRollback;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(String productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSuppressRollback() {
        return suppressRollback;
    }

    public void setSuppressRollback(boolean suppressRollback) {
        this.suppressRollback = suppressRollback;
    }

    @Override
    public String toString() {
        return "RequestInfo{" + "instanceName='" + instanceName + '\'' + ", productFamilyId='" + productFamilyId + '\''
                + ", source='" + source + '\'' + ", suppressRollback=" + suppressRollback + '}';
    }
}
