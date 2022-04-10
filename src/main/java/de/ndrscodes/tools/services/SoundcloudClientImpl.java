package de.ndrscodes.tools.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service("SoundcloudClient")
public class SoundcloudClientImpl extends ProxyServiceImpl implements SoundcloudClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    @Lazy
    private AuthenticationProviderService authenticationProviderService;

    @Override
    public ResponseEntity<String> doExecuteAuthenticated(String path, Map<String, Object> params) throws URISyntaxException {
        if(params == null)
            params = new HashMap<>();

        Map.Entry<String, String> auth = authenticationProviderService.getAuthenticationParameter();
        if(auth != null)
            params.put(auth.getKey(), auth.getValue());

        return doExecuteCall(path, params);
    }

    @Override
    public ResponseEntity<String> doExecuteAuthenticated(String path) throws URISyntaxException {
        return doExecuteAuthenticated(path, null);
    }
}
