package com.arsaka.auth.filter;

import com.arsaka.auth.controller.AuthServiceClient;
import com.arsaka.auth.exception.ApiException;
import com.arsaka.auth.exception.JwtValidException;
import com.arsaka.auth.exception.ValidationException;
import com.arsaka.auth.response.ValidateResponse;
import com.arsaka.auth.util.AuthorizationCookieUtil;
import com.arsaka.auth.util.PublicPathMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authEntryPoint;
    private final PublicPathMatcher publicPathMatcher;
    private final AuthServiceClient authServiceClient;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String token = AuthorizationCookieUtil.getTokenFromRequest(request);

            ValidateResponse validateResponse = authServiceClient.validate(token, request.getRequestURI(), request.getMethod());

            if(!validateResponse.allowed()) {
                throw new JwtValidException(validateResponse.reason().getMessage());
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            validateResponse.accountId(),
                            null,
                            validateResponse.roles().stream()
                                    .map(r -> new SimpleGrantedAuthority("ROLE_%s".formatted(r.name())))
                                    .toList()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (AuthenticationException exception) {
            authEntryPoint.commence(request, response, exception);
        } catch (ValidationException exception) {
            handleValidationException(request, response, exception);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return publicPathMatcher.isPublic(request.getRequestURI());
    }

    private void handleValidationException(HttpServletRequest request, HttpServletResponse response, ValidationException ex) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SC_SERVICE_UNAVAILABLE);

        ApiException exception = ApiException.of(
                SC_SERVICE_UNAVAILABLE,
                ex.getMessage(),
                request.getRequestURI()
        );

        mapper.writeValue(response.getWriter(), exception);
    }
}