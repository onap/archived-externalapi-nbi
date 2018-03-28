package org.onap.nbi.apis.serviceorder.model.consumer;

import java.util.List;
import java.util.Objects;

public class RequestParameters {

    private String subscriptionServiceType;

    private List<UserParams> userParams;

    private boolean aLaCarte;


    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public void setSubscriptionServiceType(String subscriptionServiceType) {
        subscriptionServiceType = subscriptionServiceType;
    }

    public List<UserParams> getUserParams() {
        return userParams;
    }

    public void setUserParams(List<UserParams> userParams) {
        this.userParams = userParams;
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
                + userParams + ", aLaCarte=" + aLaCarte + '}';
    }
}
