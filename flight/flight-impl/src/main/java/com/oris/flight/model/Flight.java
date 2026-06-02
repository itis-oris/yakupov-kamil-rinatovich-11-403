package com.oris.flight.model;

import com.oris.common.FlightStatus;
import com.oris.referencedata.model.Airplane;
import com.oris.referencedata.model.AirplaneType;
import com.oris.referencedata.model.Route;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight")
@Getter
@Setter
public class Flight {

    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_type_code", nullable = false)
    private AirplaneType airplaneType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(name = "scheduled_departure", nullable = false)
    private Instant scheduledDeparture;

    @Column(name = "scheduled_arrival", nullable = false)
    private Instant scheduledArrival;

    @Column(name = "actual_departure")
    private Instant actualDeparture;

    @Column(name = "actual_arrival")
    private Instant actualArrival;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
