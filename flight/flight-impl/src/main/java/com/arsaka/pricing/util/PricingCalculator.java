package com.arsaka.pricing.util;

import com.arsaka.pricing.dto.PricingAdjRecord;
import com.arsaka.pricing.dto.PricingRuleRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingCalculator {

    public static BigDecimal calculate(
            BigDecimal basePrice,
            PricingAdjRecord adjRecord,
            PricingRuleRecord ruleRecord
    ) {
        BigDecimal result = basePrice;


        if (adjRecord != null) {
            switch (adjRecord.getType()) {
                case ABSOLUTE -> result = result.add(adjRecord.getValue());

                case PERCENT -> result = result.multiply(
                        BigDecimal.ONE.add(
                                adjRecord.getValue()
                                        .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                        )
                );
            }
        }

        result = result.multiply(ruleRecord.getMultiplier());

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
