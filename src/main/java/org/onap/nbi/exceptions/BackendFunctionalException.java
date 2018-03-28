package org.onap.nbi.exceptions;

import org.springframework.http.HttpStatus;

public class BackendFunctionalException extends ApiException {

    private HttpStatus httpStatus;

    public BackendFunctionalException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BackendFunctionalException() {
        super();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
