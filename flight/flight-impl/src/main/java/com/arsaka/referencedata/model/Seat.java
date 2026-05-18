package com.arsaka.referencedata.model;

import com.arsaka.common.CabinClass;
import com.arsaka.search.response.dto.SeatType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "seat",
        uniqueConstraints = @UniqueConstraint(
        name = "seat-airplane_type_code-and-number-uq",
        columnNames = {"airplane_type_code", "number"}
))
@Data
public class Seat {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_type_code", nullable = false)
    private AirplaneType airplaneType;

    @Column(name = "number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class", nullable = false)
    private CabinClass cabinClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SeatType type;

    @Column(name = "has_extra_legroom", nullable = false)
    private boolean hasExtraLegroom = false;

    @Column(name = "is_exit_row", nullable = false)
    private boolean exitRow = false;
}
