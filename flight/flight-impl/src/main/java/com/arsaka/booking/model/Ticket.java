package com.arsaka.booking.model;

import com.arsaka.event.dto.DocumentType;
import com.arsaka.event.dto.PassengerGender;
import com.arsaka.common.PassengerType;
import com.arsaka.flightsearch.model.Flight;
import com.arsaka.pricing.model.Fare;
import com.arsaka.referencedata.model.Seat;
import com.arsaka.search.request.dto.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Getter
@Setter
public class Ticket {

    @Id
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "passenger_first_name", nullable = false)
    private String passengerFirstName;

    @Column(name = "passenger_last_name", nullable = false)
    private String passengerLastName;

    @Column(name = "passenger_date_of_birth", nullable = false)
    private LocalDate passengerDateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_gender", nullable = false)
    private PassengerGender passengerGender;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "document_country_code", nullable = false)
    private String documentCountryCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_type", nullable = false)
    private PassengerType passengerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fare_id", nullable = false)
    private Fare fare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
