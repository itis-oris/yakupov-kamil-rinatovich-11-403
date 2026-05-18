package com.arsaka.pricing.repository;

import com.arsaka.common.PassengerType;
import com.arsaka.jooq.tables.PricingAdjustment;
import com.arsaka.jooq.tables.PricingRule;
import com.arsaka.pricing.dto.FlightPricingRecord;
import com.arsaka.pricing.dto.PricingAdjRecord;
import com.arsaka.pricing.dto.PricingRecord;
import com.arsaka.pricing.dto.PricingRuleRecord;
import com.arsaka.pricing.exception.PricingRuleNotFoundException;
import com.arsaka.pricing.util.PricingConditionBuilder;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.arsaka.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class PricingRepository {

    private final DSLContext dsl;

    private final static PricingAdjustment pricingAdj = PRICING_ADJUSTMENT.as("pricingAdj");
    private final static PricingRule pricingRule = PRICING_RULE.as("pricingRule");

    public FlightPricingRecord getPrices(
            UUID flightId,
            UUID fareId,
            Set<PassengerType> passengers
    ) {

        List<Condition> pricingAdjConditions = PricingConditionBuilder.buildPricingAdjConditions(
                flightId,
                fareId,
                pricingAdj
        );

        List<Condition> pricingRuleConditions = PricingConditionBuilder.buildPricingRuleConditions(
                fareId,
                passengers,
                pricingRule
        );


        PricingAdjRecord pricingAdjRecord =
                dsl
                        .select(
                                pricingAdj.TYPE.as("type"),
                                pricingAdj.VALUE.as("value")
                        )
                        .from(pricingAdj)
                        .where(pricingAdjConditions)
                        .fetchOneInto(PricingAdjRecord.class);

        Set<PricingRuleRecord> pricingRuleRecords = new HashSet<>(
                dsl
                        .select(
                                pricingRule.PASSENGER_TYPE.as("passengerType"),
                                pricingRule.MULTIPLIER.as("multiplier")
                        )
                        .from(pricingRule)
                        .where(pricingRuleConditions)
                        .fetchInto(PricingRuleRecord.class)
        );

        return new FlightPricingRecord(pricingAdjRecord, pricingRuleRecords);
    }

    public PricingRecord getPrice(
            UUID flightId,
            UUID fareId,
            PassengerType passengerType
    ) {

        List<Condition> pricingAdjConditions = PricingConditionBuilder.buildPricingAdjConditions(
                flightId,
                fareId,
                pricingAdj
        );

        List<Condition> pricingRuleConditions = PricingConditionBuilder.buildPricingRuleConditions(
                fareId,
                passengerType,
                pricingRule
        );

        PricingAdjRecord pricingAdjRecord =
                dsl
                        .select(
                                pricingAdj.TYPE.as("type"),
                                pricingAdj.VALUE.as("value")
                        )
                        .from(pricingAdj)
                        .where(pricingAdjConditions)
                        .fetchOneInto(PricingAdjRecord.class);

        PricingRuleRecord pricingRuleRecord =
                dsl
                        .select(
                                pricingRule.PASSENGER_TYPE.as("passengerType"),
                                pricingRule.MULTIPLIER.as("multiplier")
                        )
                        .from(pricingRule)
                        .where(pricingRuleConditions)
                        .fetchOneInto(PricingRuleRecord.class);

        if (pricingRuleRecord == null) {
            throw new PricingRuleNotFoundException(fareId, passengerType);
        }

        return new PricingRecord(pricingAdjRecord, pricingRuleRecord);
    }


}