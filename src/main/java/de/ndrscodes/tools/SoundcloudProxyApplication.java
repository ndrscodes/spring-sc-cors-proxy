package de.ndrscodes.tools;

import de.ndrscodes.tools.handlers.NoopErrorHandler;
import de.ndrscodes.tools.services.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SoundcloudProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoundcloudProxyApplication.class, args);
	}

	@Bean
	public RetryTemplate getRetryTemplate(){
		ExceptionClassifierRetryPolicy p = new ExceptionClassifierRetryPolicy();
		p.setExceptionClassifier(new ErrorClassifier());
		return new RetryTemplateBuilder()
				.fixedBackoff(10000)
				.build();
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplateBuilder()
				.messageConverters(new StringHttpMessageConverter())
				.errorHandler(new NoopErrorHandler())
				.build();
	}
}
