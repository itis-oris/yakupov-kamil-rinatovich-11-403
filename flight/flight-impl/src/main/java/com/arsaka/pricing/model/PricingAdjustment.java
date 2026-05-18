package com.arsaka.pricing.model;

import com.arsaka.flightsearch.model.Flight;
import com.arsaka.common.PricingAdjustmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "pricing_adjustment",
    uniqueConstraints = @UniqueConstraint(
        name = "idx_pricing_adjustment_flight_id_fare_id",
        columnNames = {"flight_id", "fare_id"}
    )
)
@Data
public class PricingAdjustment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fare_id", nullable = false)
    private Fare fare;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PricingAdjustmentType type;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
