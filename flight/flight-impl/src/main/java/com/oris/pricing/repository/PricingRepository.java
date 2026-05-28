package com.oris.pricing.repository;

import com.oris.common.PassengerType;
import com.oris.jooq.tables.PricingAdjustment;
import com.oris.jooq.tables.PricingRule;
import com.oris.pricing.dto.FlightPricingRecord;
import com.oris.pricing.dto.PricingAdjRecord;
import com.oris.pricing.dto.PricingRecord;
import com.oris.pricing.dto.PricingRuleRecord;
import com.oris.pricing.exception.PricingRuleNotFoundException;
import com.oris.pricing.util.PricingConditionBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.oris.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
@Slf4j
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
            log.debug("Pricing rule not found exception | fareId={} | passengerType={}", fareId, passengerType);
            throw new PricingRuleNotFoundException();
        }

        return new PricingRecord(pricingAdjRecord, pricingRuleRecord);
    }


}