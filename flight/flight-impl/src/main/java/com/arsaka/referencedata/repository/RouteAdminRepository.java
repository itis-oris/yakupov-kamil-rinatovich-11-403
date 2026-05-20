package com.arsaka.referencedata.repository;

import com.arsaka.common.FlightStatus;
import com.arsaka.flight.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RouteAdminRepository extends JpaRepository<Route, UUID> {

    @Query("""
            SELECT r FROM Route r
            WHERE r.active = true
            AND NOT EXISTS (
                SELECT 1 FROM Flight f
                WHERE f.route.id = r.id
                AND f.status IN :activeStatuses
            )
            ORDER BY r.number
            """)
    List<Route> findActiveRoutesWithoutActiveFlights(
            @Param("activeStatuses") List<FlightStatus> activeStatuses
    );


    @Query("""
            SELECT DISTINCT r FROM Route r
            WHERE r.active = true
            AND EXISTS (
                SELECT 1 FROM Flight f1
                WHERE f1.route.id = r.id
                AND f1.status = :statusA
                AND CAST(f1.scheduledDeparture AS date) = CAST(:targetDate AS date)
            )
            AND EXISTS (
                SELECT 1 FROM Flight f2
                WHERE f2.route.id = r.id
                AND f2.status = :statusB
                AND CAST(f2.scheduledDeparture AS date) = CAST(:targetDate AS date)
            )
            ORDER BY r.number
            """)
    List<Route> findRoutesWithConflictingFlightStatuses(
            @Param("statusA") FlightStatus statusA,
            @Param("statusB") FlightStatus statusB,
            @Param("targetDate") java.time.Instant targetDate
    );
}
