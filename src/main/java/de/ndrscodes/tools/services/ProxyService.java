package de.ndrscodes.tools.services;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;

@Service
public interface ProxyService {
    public ResponseEntity<String> doExecuteCall(String path) throws URISyntaxException;
    public ResponseEntity<String> doExecuteCall(String path, Map<String, Object> params) throws URISyntaxException;
}
