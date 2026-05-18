package com.arsaka.pricing.util;

import com.arsaka.common.PassengerType;
import com.arsaka.jooq.tables.PricingAdjustment;
import com.arsaka.jooq.tables.PricingRule;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PricingConditionBuilder {

    public static List<Condition> buildPricingRuleConditions(
            UUID fareId,
            Set<PassengerType> passengerTypes,
            PricingRule pricingRule
    ) {

        List<Condition> conditions = new ArrayList<>();

        conditions.add(pricingRule.FARE_ID.eq(fareId));
        conditions.add(pricingRule.PASSENGER_TYPE.in(passengerTypes.stream().map(PassengerType::name).toList()));

        return conditions;
    }

    public static List<Condition> buildPricingAdjConditions(
            UUID flightId,
            UUID fareId,
            PricingAdjustment pricingAdj
    ) {

        List<Condition> conditions = new ArrayList<>();

        conditions.add(pricingAdj.FLIGHT_ID.eq(flightId));
        conditions.add(pricingAdj.FARE_ID.eq(fareId));

        return conditions;
    }


    public static List<Condition> buildPricingRuleConditions(
            UUID fareId,
            PassengerType passengerType,
            PricingRule pricingRule

    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(pricingRule.FARE_ID.eq(fareId));
        conditions.add(pricingRule.PASSENGER_TYPE.eq(passengerType.name()));

        return conditions;
    }



}
