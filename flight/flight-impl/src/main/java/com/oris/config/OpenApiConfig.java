package com.oris.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI flightServiceOpenAPI() {

        final String securitySchemeName = "cookieAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Flight Service API")
                        .description("Flight management service for flights searching and flight managing")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ASK Backend Team"))

                )
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name("accessToken")
                        )
                );
    }
}