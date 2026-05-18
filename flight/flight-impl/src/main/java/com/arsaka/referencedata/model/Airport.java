package com.arsaka.referencedata.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airport")
@Data
public class Airport {

    @Id
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_code", nullable = false)
    private City city;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "latitude", precision = 8, scale = 5)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 8, scale = 5)
    private BigDecimal longitude;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
