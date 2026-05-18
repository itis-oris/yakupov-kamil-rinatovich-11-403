package com.arsaka.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublicPathMatcher {

    private final List<PathPattern> publicPaths;

    public boolean isPublic(String path) {

        PathContainer container = PathContainer.parsePath(path);

        return publicPaths
                .stream()
                .anyMatch(pattern -> pattern.matches(container));
    }
}