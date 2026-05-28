package com.oris.auth.filter;

import com.oris.auth.common.AccessDeniedReason;
import com.oris.auth.service.AuthServiceClient;
import com.oris.auth.response.ValidateResponse;
import com.oris.auth.util.AuthorizationCookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static jakarta.servlet.http.HttpServletResponse.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final ObjectMapper mapper;

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/api/v1/flights/",
            "/api/v1/auth/",
            "/flights/",
            "/css/",
            "/js/",
            "/images/",
            "/favicon",
            "/.well-known/"
    );
    private static final Set<String> PUBLIC_EXACT = Set.of(
            "/",
            "/login",
            "/register"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isPublic = isPublic(path);

        String token;
        try {
            token = AuthorizationCookieUtil.getTokenFromRequest(request);
        } catch (AuthenticationTokenCookieException e) {
            if (refresh(request, response, path)) {
                return;
            }
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                handleUnauthenticated(request, response);
            }
            return;
        }

        try {
            ValidateResponse validate = authServiceClient.validate(token, path, request.getMethod());

            if (validate.allowed()) {
                setAuthentication(validate);
                chain.doFilter(request, response);
                return;
            }

            if (validate.reason() == AccessDeniedReason.TOKEN_EXPIRED) {
                if (refresh(request, response, path)) {
                    return;
                }
            }
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                handleUnauthenticated(request, response);
            }

        } catch (ValidationException e) {
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                handleServiceUnavailable(request, response, e);
            }
        } catch (Exception e) {
            log.error("JwtAuthFilter unexpected error | path={} | message={}", path, e.getMessage(), e);
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                handleUnauthenticated(request, response);
            }
        }
    }

    private boolean refresh(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        String refreshToken = extractRefreshCookie(request);
        if (refreshToken != null) {
            boolean refreshed = tryRefresh(refreshToken, response);
            if (refreshed) {
                if (isHtmlRequest(request)) {
                    response.sendRedirect(path);
                } else {
                    response.setStatus(SC_UNAUTHORIZED);
                    response.setHeader("X-Token-Refreshed", "true");
                }
                return true;
            }
        }
        return false;
    }

    private void setAuthentication(ValidateResponse v) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                v.accountId(), null,
                v.roles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_%s".formatted(r.name())))
                        .toList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean tryRefresh(String refreshToken, HttpServletResponse response) {
        try {
            ResponseEntity<Void> refreshResponse = authServiceClient.refresh(refreshToken);
            List<String> cookies = refreshResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (cookies != null) {
                cookies.forEach(c -> response.addHeader(HttpHeaders.SET_COOKIE, c));
            }
            log.debug("JwtAuthFilter | token refreshed successfully");
            return true;
        } catch (Exception e) {
            log.debug("JwtAuthFilter | refresh failed: {}", e.getMessage());
            return false;
        }
    }

    private String extractRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }

    private boolean isPublic(String path) {
        if (PUBLIC_EXACT.contains(path)) return true;
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        return false;
    }

    private boolean isHtmlRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }

    private void handleUnauthenticated(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        if (isHtmlRequest(request)) {
            response.sendRedirect("/login");
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(SC_UNAUTHORIZED);
            mapper.writeValue(response.getWriter(),
                    ApiException.of(SC_UNAUTHORIZED, "Unauthorized", request.getRequestURI()));
        }
    }

    private void handleServiceUnavailable(HttpServletRequest request,
                                          HttpServletResponse response,
                                          ValidationException ex) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SC_SERVICE_UNAVAILABLE);
        mapper.writeValue(response.getWriter(),
                ApiException.of(SC_SERVICE_UNAVAILABLE, ex.getMessage(), request.getRequestURI()));
    }
}
