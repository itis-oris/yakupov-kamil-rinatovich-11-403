package com.oris.pricing.model;

import com.oris.common.CabinClass;
import com.oris.referencedata.model.Airline;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fare")
@Getter
@Setter
public class Fare {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_code", nullable = false)
    private Airline airline;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class", nullable = false)
    private CabinClass cabinClass;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "baggage_included", nullable = false)
    private boolean baggageIncluded = false;

    @Builder.Default
    @Column(name = "is_refundable", nullable = false)
    private boolean refundable = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return Objects.equals(id, fare.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
