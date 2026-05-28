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

//        final String securitySchemeName = "bearerAuth";
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Flight Service API")
//                        .description("Flight management service is part of a custom tour-building platform")
//                        .version("1.0.0")
//                        .contact(new Contact()
//                                .name("ASK Backend Team"))
//                )
//                .schemaRequirement(
//                        securitySchemeName,
//                        new SecurityScheme()
//                                .name(securitySchemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                );

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