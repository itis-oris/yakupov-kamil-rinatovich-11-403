package com.arsaka.auth.service;

import com.arsaka.auth.common.AccountRole;
import com.arsaka.auth.exception.*;
import lombok.RequiredArgsConstructor;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.model.Account;
import com.arsaka.auth.common.AccountStatus;
import com.arsaka.auth.model.Role;
import com.arsaka.auth.repository.AccountRepository;
import com.arsaka.auth.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Account findAccountById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            log.debug("Account not found exception");
            return new AccountNotFoundException();
        });
    }


    public Account updateUsername(UUID accountId, String username) {
        Account account = findAccountById(accountId);
        if (accountRepository.existsByUsername(username)) {
            log.warn("username already exists | username={}", username);
            throw new UsernameAlreadyExistsException();
        }

        account.setUsername(username);
        return account;
    }

    public Account save(RegisterRequest registerRequest) {
        String email = registerRequest.email();

        if (accountRepository.existsByEmail(email)) {
            log.warn("Email already exists | email={}", email);
            throw new EmailAlreadyExistsException();
        }

        if (accountRepository.existsByUsername(registerRequest.username())) {
            log.warn("username already exists | username={}", registerRequest.username());
            throw new UsernameAlreadyExistsException();
        }

        Role role = roleRepository.findByName(AccountRole.USER).orElseThrow(() -> {
                    log.error("no such default account role exception | role={}", AccountRole.USER);
                    return new NoSuchDefaultAccountRoleException();
                }
        );
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
            throw new AccountNotActiveException();
        }
    }
}
