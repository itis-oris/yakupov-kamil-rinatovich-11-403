package com.arsaka.referencedata.model;

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
@Table(name = "airplane")
@Data
public class Airplane {

    @Id
    @GeneratedValue
    private UUID id;

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
}
