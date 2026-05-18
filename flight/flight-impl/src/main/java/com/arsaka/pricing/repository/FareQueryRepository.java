package com.arsaka.pricing.repository;

import com.arsaka.common.CabinClass;
import com.arsaka.search.response.dto.FlightFare;
import com.arsaka.jooq.tables.Fare;
import com.arsaka.pricing.util.FareConditionBuilder;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.arsaka.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class FareQueryRepository {

    private final DSLContext dsl;

    private final static Fare fare = FARE.as("fear");

    public Set<FlightFare> getFares(String airlineCode, CabinClass cabinClass) {
        List<Condition> conditions = FareConditionBuilder.build(airlineCode, cabinClass, fare);

        return new HashSet<>(
                dsl
                        .select(
                                fare.ID.as("fareId"),
                                fare.AIRLINE_CODE.as("airlineCode"),
                                fare.CABIN_CLASS.as("cabinClass"),
                                fare.NAME.as("name"),
                                fare.BAGGAGE_INCLUDED.as("isBaggageIncluded"),
                                fare.IS_REFUNDABLE.as("isRefundable")
                        )
                        .from(fare)
                        .where(conditions)
                        .fetchInto(FlightFare.class)
        );
    }

    public FlightFare getFare(UUID fareId) {

        return dsl
                .select(
                        fare.ID.as("fareId"),
                        fare.AIRLINE_CODE.as("airlineCode"),
                        fare.CABIN_CLASS.as("cabinClass"),
                        fare.NAME.as("name"),
                        fare.BAGGAGE_INCLUDED.as("isBaggageIncluded"),
                        fare.IS_REFUNDABLE.as("isRefundable")
                )
                .from(fare)
                .where(fare.ID.eq(fareId))
                .fetchOneInto(FlightFare.class);
    }

    public CabinClass getCabinClass(UUID fareId) {

        return dsl
                .select(
                        fare.CABIN_CLASS
                )
                .from(fare)
                .where(fare.ID.eq(fareId))
                .fetchOneInto(CabinClass.class);
    }
}
