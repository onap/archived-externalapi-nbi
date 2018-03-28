package org.onap.nbi.apis.serviceorder.model.consumer;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RequestState {

    FAILED("FAILED"),

    COMPLETE("COMPLETE");

    private String value;

    RequestState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RequestState fromValue(String text) {
        for (RequestState b : RequestState.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }



}