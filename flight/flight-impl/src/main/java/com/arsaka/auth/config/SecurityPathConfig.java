package com.arsaka.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityPathConfig {

    private final SecurityProperties properties;
    private final PathPatternParser parser = new PathPatternParser();

    @Bean
    public List<PathPattern> securityPaths() {
        return properties.publicPaths().stream().map(parser::parse).toList();
    }
}
