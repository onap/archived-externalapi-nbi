package org.onap.nbi.exceptions;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import java.io.IOException;

public class BackendErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() != null) {
            throw new BackendFunctionalException(response.getStatusCode(), response.getStatusText());
        }
    }
}
