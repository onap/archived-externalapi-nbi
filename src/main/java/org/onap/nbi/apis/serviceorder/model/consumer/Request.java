package org.onap.nbi.apis.serviceorder.model.consumer;

import java.util.Objects;

public class Request {

    private RequestStatus requestStatus;

    public Request() {}

    public Request(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Request request = (Request) o;
        return Objects.equals(requestStatus, request.requestStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestStatus);
    }

    @Override
    public String toString() {
        return "Request{" + "requestStatus=" + requestStatus + '}';
    }
}
