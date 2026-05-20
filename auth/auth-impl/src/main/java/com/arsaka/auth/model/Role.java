package com.arsaka.auth.model;

import com.arsaka.auth.common.AccountRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private AccountRole name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
