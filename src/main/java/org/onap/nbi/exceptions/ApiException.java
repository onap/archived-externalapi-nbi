package org.onap.nbi.exceptions;

import java.io.Serializable;

public class ApiException extends RuntimeException implements Serializable {

    protected final String localisationClass;
    protected final String localisationMethod;

    public ApiException() {
        super();
        localisationClass = "";
        localisationMethod = "";
    }

    public ApiException(String message) {
        super(message);
        localisationClass = "";
        localisationMethod = "";
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        localisationClass = "";
        localisationMethod = "";
    }

    public ApiException(Throwable cause) {
        super(cause);
        localisationClass = "";
        localisationMethod = "";
    }
}
