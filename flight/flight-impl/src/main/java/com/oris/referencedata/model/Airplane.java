package com.oris.referencedata.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airplane")
@Getter
@Setter
public class Airplane {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_type_code", nullable = false)
    private AirplaneType airplaneType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_code", nullable = false)
    private Airline airline;

    @Column(name = "manufactured_year", nullable = false)
    private int manufacturedYear;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airplane airplane = (Airplane) o;
        return Objects.equals(id, airplane.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
