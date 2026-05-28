package com.oris.pricing.model;

import com.oris.common.PassengerType;
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
    name = "pricing_rule",
    uniqueConstraints = @UniqueConstraint(
        name = "pricing_rule_fare_id-and-passenger_type-uq",
        columnNames = {"fare_id", "passenger_type"}
    )
)
@Getter
@Setter
public class PricingRule {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fare_id", nullable = false)
    private Fare fare;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_type", nullable = false)
    private PassengerType passengerType;

    @Column(name = "multiplier", nullable = false, precision = 5, scale = 2)
    private BigDecimal multiplier;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PricingRule that = (PricingRule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
