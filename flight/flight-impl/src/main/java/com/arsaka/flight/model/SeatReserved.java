package com.arsaka.flight.model;

import com.arsaka.referencedata.model.Seat;
import com.arsaka.search.response.dto.SeatReservedStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
    name = "seat_reserved",
    uniqueConstraints = @UniqueConstraint(
        name = "seat_reserved_flight_id-and-seat_id-uq",
        columnNames = {"flight_id", "seat_id"}
    )
)
@Getter
@Setter
public class SeatReserved {

    @Id
    private UUID id = UUID.randomUUID();

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SeatReserved that = (SeatReserved) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
