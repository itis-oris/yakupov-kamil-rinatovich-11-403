package com.arsaka.pricing.model;

import com.arsaka.flight.model.Flight;
import com.arsaka.common.PricingAdjustmentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
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
@Getter
@Setter
public class PricingAdjustment {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PricingAdjustment that = (PricingAdjustment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
