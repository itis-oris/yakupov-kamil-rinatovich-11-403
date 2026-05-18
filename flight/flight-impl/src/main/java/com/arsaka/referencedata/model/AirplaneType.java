package com.arsaka.referencedata.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airplane_type")
@Data
public class AirplaneType {

    @Id
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;
}
