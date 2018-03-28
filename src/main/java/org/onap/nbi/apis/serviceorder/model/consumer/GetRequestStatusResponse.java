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

    public GetRequestStatusResponse() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetRequestStatusResponse that = (GetRequestStatusResponse) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }

    @Override
    public String toString() {
        return "GetRequestStatusResponse{" +
                "request=" + request +
                '}';
    }
}
