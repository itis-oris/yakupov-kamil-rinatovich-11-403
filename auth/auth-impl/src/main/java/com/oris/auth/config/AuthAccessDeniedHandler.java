package com.oris.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.oris.auth.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;


@RequiredArgsConstructor
@Slf4j
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SC_FORBIDDEN);

        ApiException exception = ApiException.of(
                SC_FORBIDDEN,
                accessDeniedException.getMessage(),
                request.getRequestURI()
        );

        log.debug("Access Denied Exception | message={}", accessDeniedException.getMessage());

        mapper.writeValue(response.getWriter(), exception);
    }
}
