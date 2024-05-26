package com.warehousemanagement.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfiguration.class);

    private static final String authorizationTokenKey = "Authorization token";

    /**
     * Version number/information for the application
     */
    @Value("${application.version}")
    private String applicationVersion;

    /**
     * log info
     */
    public SwaggerConfiguration() {
        SwaggerConfiguration.LOGGER.info("SwaggerConfiguration is initialized.");
    }

    @Bean
    public GroupedOpenApi swaggerApplicationApi() {
        return GroupedOpenApi.builder()
                .group("warehouse-rest-api")
                .pathsToMatch("/rest/**")
                .build();
    }

    /**
     * Get the application Rest Api Info data.
     * Adds the global custom headers for Authorization and Refresh-Authorization tokens
     *
     * @return the application Rest Api Info data.
     */
    @Bean
    public OpenAPI apiInfo() {
        Info info = new Info()
                .title("Warehouse Management Application API")
                .description("This site provides an overview of all application endpoints")
                .license(new License().url("http://www.google.com/"))
                .version(applicationVersion);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(authorizationTokenKey);
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer");

        Components components = new Components()
                .addSecuritySchemes(authorizationTokenKey, securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
