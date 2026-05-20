package com.arsaka.referencedata.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airline")
@Getter
@Setter
public class Airline {

    @Id
    @Column(name = "code", length = 2, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airline airline = (Airline) o;
        return Objects.equals(code, airline.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
