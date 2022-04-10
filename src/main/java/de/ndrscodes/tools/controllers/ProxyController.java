package de.ndrscodes.tools.controllers;

import de.ndrscodes.tools.services.ProxyService;
import de.ndrscodes.tools.services.SoundcloudClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProxyController {

    @Autowired
    @Qualifier("SoundcloudClient")
    private SoundcloudClientImpl client;
    @Autowired
    @Qualifier("Proxy")
    private ProxyService proxy;

    @RequestMapping("hack/**")
    public ResponseEntity<String> proxy(HttpServletRequest request){
        try {
            String uri = request.getRequestURI().trim();
            if(uri.startsWith("/hack/")){
                uri = uri.substring(6);
            }

            Map<String, Object> params = new HashMap<>(request.getParameterMap());

            ResponseEntity<String> res =
                    client.doExecuteAuthenticated(uri, params);
            return processResponse(res);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping("/**")
    public ResponseEntity<String> proxySoundcloudPage(HttpServletRequest request){
        try {
            String uri = request.getRequestURI().trim();
            Map<String, Object> params = new HashMap<>(request.getParameterMap());
            ResponseEntity<String> res =
                    proxy.doExecuteCall(uri, params);
            return processResponse(res);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ResponseEntity<String> processResponse(ResponseEntity<String> response){
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(response.getHeaders());

        headers.setAccessControlAllowOrigin("*"); //just let us access this thing from anywhere.
        return ResponseEntity.status(response.getStatusCode()).headers(headers).body(response.getBody());
    }
}
