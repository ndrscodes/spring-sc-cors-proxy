package de.ndrscodes.tools.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationProviderServiceImpl implements AuthenticationProviderService {
    private static String secret = null;
    private static final Pattern SCRIPT_SRC_PATTERN = Pattern.compile("<script (?:crossorigin )?src=\"(.*)\"(?: crossorigin)?>", Pattern.MULTILINE);
    private final static Pattern CLIENT_ID_PATTERN = Pattern.compile("client_id[=:]\"?(\\w+)\"", Pattern.MULTILINE);

    @Autowired
    @Qualifier("SoundcloudClient")
    SoundcloudClient scClient;

    @Autowired
    @Qualifier("Proxy")
    ProxyService proxy;

    @Value("${soundcloud.url:https://soundcloud.com}")
    private String soundcloudUrl;

    @Override
    public Map.Entry<String, String> getAuthenticationParameter() {
        if(secret == null){
            try {
                secret = findSecret();
            } catch (Exception e) {
                secret = "";
            }
        }

        return secret == null ? null : Map.entry("client_id", secret);
    }

    private String findSecret() throws URISyntaxException {
        ResponseEntity<String> ctx = scClient.doExecuteCall(soundcloudUrl);
        String body = ctx.getBody();

        if(body == null) return null;

        Matcher m = SCRIPT_SRC_PATTERN.matcher(body);
        while(m.find()){
            String path = m.group(1);
            body = proxy.doExecuteCall(path).getBody();
            if(body == null || body.length() < 12) continue; //skip if it does not at least contain client_id plus three other characters

            Matcher parser = CLIENT_ID_PATTERN.matcher(body);
            if(parser.find()){
                return parser.group(1);
            }
        }
        return null;
    }

    public void reset(){
        secret = null;
    }
}
