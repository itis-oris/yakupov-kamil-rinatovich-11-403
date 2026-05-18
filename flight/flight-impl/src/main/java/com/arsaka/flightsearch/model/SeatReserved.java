package com.arsaka.flightsearch.model;

import com.arsaka.referencedata.model.Seat;
import com.arsaka.search.response.dto.SeatReservedStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "seat_reserved",
    uniqueConstraints = @UniqueConstraint(
        name = "seat_reserved_flight_id-and-seat_id-uq",
        columnNames = {"flight_id", "seat_id"}
    )
)
@Data
public class SeatReserved {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatReservedStatus status;

    @Column(name = "held_by_booking_id")
    private UUID heldByBookingId;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;
}
