package com.arsaka.referencedata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "airplane_type")
@Getter
@Setter
public class AirplaneType {

    @Id
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AirplaneType that = (AirplaneType) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
