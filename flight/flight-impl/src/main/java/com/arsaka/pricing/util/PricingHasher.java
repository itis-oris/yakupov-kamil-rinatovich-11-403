package com.arsaka.pricing.util;

import com.arsaka.pricing.dto.PricingAdjRecord;
import com.arsaka.pricing.dto.PricingRuleRecord;
import com.arsaka.pricing.exception.PriceHashException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.HexFormat;
import java.util.UUID;

public class PricingHasher {

    //TODO: вынести в переменные окружения
    private static final String SECRET = "my-secret";
    private static final String SEPARATOR_CHAR = ";";


    public static String hash(
            UUID flightId,
            UUID fareId,
            BigDecimal basePrice,
            PricingRuleRecord ruleRecord,
            PricingAdjRecord adjRecord


    ) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(flightId)
                .append(SEPARATOR_CHAR)
                .append(fareId)
                .append(SEPARATOR_CHAR)
                .append(basePrice)
                .append(SEPARATOR_CHAR)
                .append(ruleRecord.getPassengerType())
                .append(SEPARATOR_CHAR)
                .append(ruleRecord.getMultiplier());

        if(adjRecord != null) {
            sb
                    .append(SEPARATOR_CHAR)
                    .append(adjRecord.getType())
                    .append(SEPARATOR_CHAR)
                    .append(adjRecord.getValue());
        }

        String data = sb.toString();
        
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(), "HmacSHA256"));
            byte[] result = mac.doFinal(data.getBytes());
            return HexFormat.of().formatHex(result);
        } catch (Exception e) {
            throw new PriceHashException(e.getMessage());
        }
    }
}