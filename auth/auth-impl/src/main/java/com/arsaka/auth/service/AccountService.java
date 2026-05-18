package com.arsaka.auth.service;

import com.arsaka.auth.common.AccountRole;
import com.arsaka.auth.exception.*;
import lombok.RequiredArgsConstructor;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.model.Account;
import com.arsaka.auth.model.AccountStatus;
import com.arsaka.auth.model.Role;
import com.arsaka.auth.repository.AccountRepository;
import com.arsaka.auth.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Account findAccountById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account save(RegisterRequest registerRequest) {
        String email = registerRequest.email();

        if (accountRepository.existsByEmail(email)) {
            log.warn("Email already exists | email={}", email);
            throw new EmailAlreadyExistsException(email);
        }

        if (accountRepository.existsByUsername(registerRequest.username())) {
            log.warn("username already exists | username={}", registerRequest.username());
            throw new UsernameAlreadyExistsException(registerRequest.username());
        }

        Role role = roleRepository.findByName(AccountRole.USER).orElseThrow(() -> new NoSuchDefaultAccountRoleException(AccountRole.USER));
        Account account = new Account();
        account.setEmail(email);
        account.setUsername(registerRequest.username());
        account.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        account.getRoles().add(role);
        return accountRepository.save(account);
    }

    public void isActive(Account account) {
        if(account.getStatus() != AccountStatus.ACTIVE) {
            log.warn("Account not active | accountId={} | status={}", account.getId(), account.getStatus());
            throw new AccountNotActiveException(account.getId());
        }
    }
}
