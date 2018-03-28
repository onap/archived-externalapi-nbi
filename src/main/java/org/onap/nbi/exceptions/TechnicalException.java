package org.onap.nbi.exceptions;

import org.springframework.http.HttpStatus;

public class TechnicalException extends ApiException {

    private HttpStatus httpStatus;

    public TechnicalException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public TechnicalException() {
        super();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}