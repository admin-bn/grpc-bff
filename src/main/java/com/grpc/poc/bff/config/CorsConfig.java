package com.grpc.poc.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@Configuration
public class CorsConfig {

	String[] exposedHeaders = { "Authorization", "userId", "session_state" };
	String[] allowedOriginPatterns = { "*" };

	@Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurerComposite() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
			}
		};
	}

}
