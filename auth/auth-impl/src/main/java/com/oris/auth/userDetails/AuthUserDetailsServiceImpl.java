package com.oris.auth.userDetails;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.oris.auth.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AuthUserDetailsImpl(
                accountRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Account with email: %s not found".formatted(email)))
        );
    }
}
