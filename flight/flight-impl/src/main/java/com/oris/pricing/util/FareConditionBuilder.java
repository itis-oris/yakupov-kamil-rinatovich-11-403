package com.oris.pricing.util;

import com.oris.common.CabinClass;
import com.oris.jooq.tables.Fare;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;

public class FareConditionBuilder {

    public static List<Condition> build(
            String airlineCode,
            CabinClass cabinClass,
            Fare fare
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(fare.AIRLINE_CODE.eq(airlineCode));
        conditions.add(fare.CABIN_CLASS.eq(cabinClass.name()));

        return conditions;
    }




}
