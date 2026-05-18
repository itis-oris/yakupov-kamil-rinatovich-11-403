package com.arsaka.auth.service;

import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.model.Account;
import com.arsaka.auth.userDetails.AuthUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationSecurityService {

    private final AuthenticationManager authManager;

    public Account authenticate(LoginRequest loginRequest) {
        return ((AuthUserDetailsImpl) authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.email(),
                                loginRequest.password())
                )
                .getPrincipal()
        ).account();
    }
}
