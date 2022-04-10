package de.ndrscodes.tools.services;

import org.springframework.http.RequestEntity;

import java.util.Map;

public interface AuthenticationProviderService {
    public Map.Entry<String, String> getAuthenticationParameter();
    public void reset();
}
