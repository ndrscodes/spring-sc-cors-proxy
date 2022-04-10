package de.ndrscodes.tools;

import de.ndrscodes.tools.services.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
public class ErrorClassifier implements Classifier<Throwable, RetryPolicy> {

    private static final RetryPolicy NO_RETRY_POLICY = new NeverRetryPolicy();
    private static final RetryPolicy RETRY_POLICY = new SimpleRetryPolicy(2);

    @Autowired
    private AuthenticationProviderService authProvider;

    @Override
    public RetryPolicy classify(Throwable classifiable) {
        if(classifiable instanceof HttpStatusCodeException){
            if(((HttpStatusCodeException)classifiable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
                return RETRY_POLICY;
            }
            if(((HttpStatusCodeException)classifiable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
                authProvider.reset();
                return RETRY_POLICY;
            }
        }
        return NO_RETRY_POLICY;
    }
}
