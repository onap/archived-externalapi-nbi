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

package org.onap.nbi.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BackendFunctionalException.class)
    @ResponseBody
    public ResponseEntity<ApiError> backendExceptionHandler(final BackendFunctionalException exception) {
        ApiError apiError =
                new ApiError(String.valueOf(exception.getHttpStatus().value()), exception.getMessage(), "", "");
        return new ResponseEntity<>(apiError, exception.getHttpStatus());
    }

    @ExceptionHandler(TechnicalException.class)
    @ResponseBody
    public ResponseEntity<ApiError> technicalExceptionHandler(final TechnicalException exception) {
        ApiError apiError =
                new ApiError(String.valueOf(exception.getHttpStatus().value()), exception.getMessage(), "", "");
        return new ResponseEntity<>(apiError, exception.getHttpStatus());
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseBody
    public ResponseEntity<ApiError> restClientExceptionHandler(final RestClientException exception) {
        ApiError apiError = new ApiError("500", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unable to " + "reach ONAP services", "");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ApiError> validationExceptionHandler(final ValidationException exception) {
        ApiError apiError = new ApiError("400", HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessages(), "");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseBody
    public ResponseEntity<ApiError> validationExceptionHandler(final JsonProcessingException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Request data is invalid!", "");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
