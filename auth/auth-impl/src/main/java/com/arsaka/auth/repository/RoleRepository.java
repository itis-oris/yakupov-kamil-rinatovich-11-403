package com.arsaka.auth.repository;

import com.arsaka.auth.common.AccountRole;
import com.arsaka.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(AccountRole name);
}
