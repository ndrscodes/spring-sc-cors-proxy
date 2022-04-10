package de.ndrscodes.tools.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service("Proxy")
public class ProxyServiceImpl implements ProxyService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Override
    public ResponseEntity<String> doExecuteCall(String path) throws URISyntaxException {
        return doExecuteCall(path, null);
    }

    @Override
    public ResponseEntity<String> doExecuteCall(String path, Map<String, Object> params) throws URISyntaxException {
        path = path == null ? "" : path.trim();

        while(path.startsWith("/") && path.length() > 2)
            path = path.substring(1);

        if(!path.contains("://")){
            path = "https://" + path;
        }

        RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, new URI(path));

        if(params == null){
            params = new HashMap<>();
        }

        UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(path);
        for(Map.Entry<String, Object> entry : params.entrySet()){
            b.queryParam(entry.getKey(), "{" + entry.getKey() + "}");
        }
        String finalPath = b.encode().toUriString();
        Map<String, Object> finalParams = params;
        return retryTemplate.execute(ctx -> restTemplate.getForEntity(finalPath, String.class, finalParams));
    }
}
