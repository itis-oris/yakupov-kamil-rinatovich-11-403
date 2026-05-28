package com.oris.referencedata.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airport")
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
