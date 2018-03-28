package org.onap.nbi.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);


    @ExceptionHandler(BackendFunctionalException.class)
    @ResponseBody
    public ResponseEntity<ApiError> backendExceptionHandler(final BackendFunctionalException exception) {
        ApiError apiError = new ApiError(String.valueOf(exception.getHttpStatus().value()), exception.getMessage(), "", "");
        return new ResponseEntity<ApiError>(apiError, exception.getHttpStatus());
    }

    @ExceptionHandler(TechnicalException.class)
    @ResponseBody
    public ResponseEntity<ApiError> technicalExceptionHandler(final TechnicalException exception) {
        ApiError apiError = new ApiError(String.valueOf(exception.getHttpStatus().value()), exception.getMessage(), "", "");
        return new ResponseEntity<ApiError>(apiError, exception.getHttpStatus());
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseBody
    public ResponseEntity<ApiError> RestClientExceptionHandler(final RestClientException exception) {
        ApiError apiError = new ApiError("500", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Unable to " +
                "reach ONAP services", "");
        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ApiError> ValidationExceptionHandler(final ValidationException exception) {
        ApiError apiError = new ApiError("400", HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessages(), "");
        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
