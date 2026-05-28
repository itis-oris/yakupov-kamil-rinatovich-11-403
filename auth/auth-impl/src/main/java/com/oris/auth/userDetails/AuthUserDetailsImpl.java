package com.oris.auth.userDetails;

import com.oris.auth.model.Account;
import com.oris.auth.common.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record AuthUserDetailsImpl(Account account) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_%s".formatted(r.getName())))
                .toList();
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getStatus() != AccountStatus.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }
}
