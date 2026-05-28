package com.oris.referencedata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country {

    @Id
    @Column(name = "code", length = 2, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(code, country.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
