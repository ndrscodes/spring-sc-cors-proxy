package de.ndrscodes.tools.handlers;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class NoopErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response){
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }
}
