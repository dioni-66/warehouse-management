package com.warehousemanagement.configuration.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CorsConfigurationSource for configuring a Cross-Origin Resource Sharing (CORS).
 */
public class WebCorsConfigurationSource extends UrlBasedCorsConfigurationSource {

    private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:8080");

    /**
     * Constructor of CorsConfigurationSource
     **/
    public WebCorsConfigurationSource() {
        configure();
    }

    /**
     * Initialize the CorsConfiguration
     *
     * @return a configured CorsConfiguration
     */
    private CorsConfiguration initConfiguration() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));

        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-FORWARDED-FOR"));
        configuration.setExposedHeaders(List.of("Authorization"));

        return configuration;
    }

    /**
     * Configure and initialize the CORS source for a specific path
     */
    private void configure() {
        final CorsConfiguration configuration = initConfiguration();
        registerCorsConfiguration("/rest/**", configuration);
    }

}
