package de.ndrscodes.tools.services;

import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Map;

public interface SoundcloudClient extends ProxyService{
    ResponseEntity<String> doExecuteAuthenticated(String path, Map<String, Object> params) throws URISyntaxException;
    ResponseEntity<String> doExecuteAuthenticated(String path) throws URISyntaxException;
}
