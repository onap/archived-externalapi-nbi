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

import java.util.Objects;

public class GetRequestStatusResponse {

    private Request request;


    public Request getRequest() {

        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public GetRequestStatusResponse(Request request) {

        this.request = request;
    }

    public GetRequestStatusResponse() {}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GetRequestStatusResponse that = (GetRequestStatusResponse) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }

    @Override
    public String toString() {
        return "GetRequestStatusResponse{" + "request=" + request + '}';
    }
}
