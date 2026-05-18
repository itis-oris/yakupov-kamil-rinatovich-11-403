package com.arsaka.pricing.model;

import com.arsaka.common.CabinClass;
import com.arsaka.referencedata.model.Airline;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fare")
@Data
public class Fare {

    @Id
    @GeneratedValue
    private UUID id;

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
}
