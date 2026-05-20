package com.arsaka.flightsearch.model;

import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.model.Airport;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route")
@Getter
@Setter
public class Route {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_code", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_code", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_code", nullable = false)
    private Airport arrivalAirport;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
