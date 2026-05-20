package com.arsaka.referencedata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "city")
@Getter
@Setter
public class City {

    @Id
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(code, city.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
