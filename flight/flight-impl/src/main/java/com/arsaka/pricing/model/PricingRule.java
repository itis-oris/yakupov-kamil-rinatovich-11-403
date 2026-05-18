package com.arsaka.pricing.model;

import com.arsaka.common.PassengerType;
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
    name = "pricing_rule",
    uniqueConstraints = @UniqueConstraint(
        name = "pricing_rule_fare_id-and-passenger_type-uq",
        columnNames = {"fare_id", "passenger_type"}
    )
)
@Data
public class PricingRule {

    @Id
    @GeneratedValue
    private UUID id;

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
}
