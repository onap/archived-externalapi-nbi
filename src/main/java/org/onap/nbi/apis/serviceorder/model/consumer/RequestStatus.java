package org.onap.nbi.apis.serviceorder.model.consumer;

import java.util.Objects;

public class RequestStatus {

    private RequestState requestState;

    private int percentProgress;

    public RequestStatus(RequestState requestState, int percentProgress) {
        this.requestState = requestState;
        this.percentProgress = percentProgress;
    }

    public RequestStatus() {}

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public int getPercentProgress() {
        return percentProgress;
    }

    public void setPercentProgress(int percentProgress) {
        this.percentProgress = percentProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RequestStatus that = (RequestStatus) o;
        return percentProgress == that.percentProgress && requestState == that.requestState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestState, percentProgress);
    }

    @Override
    public String toString() {
        return "RequestStatus{" + "requestState=" + requestState + ", percentProgress=" + percentProgress + '}';
    }
}
