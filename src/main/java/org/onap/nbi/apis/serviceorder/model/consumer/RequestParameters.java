/**
 *     Copyright (c) 2018 Orange
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

import java.util.List;
import java.util.Objects;

public class RequestParameters {

    private String subscriptionServiceType;

    private List<UserParams> userParams;

    private List<Object> userParamList;
    
    private boolean aLaCarte;

    private String testApi;

    public String getTestApi() {
        return testApi;
    }

    public void setTestApi(String testApi) {
        this.testApi = testApi;
    }

    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public void setSubscriptionServiceType(String subscriptionServiceType) {
        this.subscriptionServiceType = subscriptionServiceType;
    }

    public List<UserParams> getUserParams() {
        return userParams;
    }

    public void setUserParams(List<UserParams> userParams) {
        this.userParams = userParams;
    }

    public List<Object> getUserParamList() {
      return userParamList;
    }
  
    public void setUserParamList(List<Object> userParamList) {
      this.userParamList = userParamList;
    }
  
    public boolean isaLaCarte() {
        return aLaCarte;
    }

    public void setaLaCarte(boolean aLaCarte) {
        this.aLaCarte = aLaCarte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RequestParameters that = (RequestParameters) o;
        return aLaCarte == that.aLaCarte && Objects.equals(subscriptionServiceType, that.subscriptionServiceType)
                && Objects.equals(userParams, that.userParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionServiceType, userParams, aLaCarte);
    }

    @Override
    public String toString() {
        return "RequestParameters{" + "subscriptionServiceType='" + subscriptionServiceType + '\'' + ", userParams="
                + userParams + '\'' + ", userParamList="
                        + userParamList + ", aLaCarte=" + aLaCarte + ", testApi='" + testApi + '\'' + '}';
    }
}