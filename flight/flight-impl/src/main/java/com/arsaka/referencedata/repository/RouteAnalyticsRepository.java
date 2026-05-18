package com.arsaka.referencedata.repository;

import com.arsaka.flightsearch.model.Route;
import com.arsaka.referencedata.model.Airport;
import com.arsaka.referencedata.model.City;
import com.arsaka.referencedata.model.Country;
import com.arsaka.search.request.RouteFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RouteAnalyticsRepository {

    private final EntityManager em;

    public List<Route> findRoutesWithFilter(RouteFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Route> query = cb.createQuery(Route.class);
        Root<Route> route = query.from(Route.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.airlineCode() != null) {
            predicates.add(cb.equal(route.get("airline").get("code"), filter.airlineCode()));
        }
        if (filter.departureAirportCode() != null) {
            predicates.add(cb.equal(route.get("departureAirport").get("code"), filter.departureAirportCode()));
        }
        if (filter.arrivalAirportCode() != null) {
            predicates.add(cb.equal(route.get("arrivalAirport").get("code"), filter.arrivalAirportCode()));
        }
        if (filter.active() != null) {
            predicates.add(cb.equal(route.get("active"), filter.active()));
        }

        if (filter.departureCountryCode() != null) {
            Subquery<String> depCountrySub = query.subquery(String.class);
            Root<Airport> depAirport = depCountrySub.from(Airport.class);
            Join<Airport, City> depCity = depAirport.join("city", JoinType.INNER);
            Join<City, Country> depCountry = depCity.join("country", JoinType.INNER);
            depCountrySub
                    .select(depAirport.get("code"))
                    .where(
                            cb.equal(depCountry.get("code"), filter.departureCountryCode()),
                            cb.isTrue(depAirport.get("active"))
                    );
            predicates.add(route.get("departureAirport").get("code").in(depCountrySub));
        }

        if (filter.arrivalCountryCode() != null) {
            Subquery<String> arrCountrySub = query.subquery(String.class);
            Root<Airport> arrAirport = arrCountrySub.from(Airport.class);
            Join<Airport, City> arrCity = arrAirport.join("city", JoinType.INNER);
            Join<City, Country> arrCountry = arrCity.join("country", JoinType.INNER);
            arrCountrySub
                    .select(arrAirport.get("code"))
                    .where(
                            cb.equal(arrCountry.get("code"), filter.arrivalCountryCode()),
                            cb.isTrue(arrAirport.get("active"))
                    );
            predicates.add(route.get("arrivalAirport").get("code").in(arrCountrySub));
        }

        if (filter.cityCode() != null) {
            Subquery<String> depCitySub = query.subquery(String.class);
            Root<Airport> depAirportCity = depCitySub.from(Airport.class);
            depCitySub
                    .select(depAirportCity.get("code"))
                    .where(cb.equal(
                            cb.lower(depAirportCity.get("city").get("code")),
                            filter.cityCode().toLowerCase()
                    ));

            Subquery<String> arrCitySub = query.subquery(String.class);
            Root<Airport> arrAirportCity = arrCitySub.from(Airport.class);
            arrCitySub
                    .select(arrAirportCity.get("code"))
                    .where(cb.equal(
                            cb.lower(arrAirportCity.get("city").get("code")),
                            filter.cityCode().toLowerCase()
                    ));

            predicates.add(cb.or(
                    route.get("departureAirport").get("code").in(depCitySub),
                    route.get("arrivalAirport").get("code").in(arrCitySub)
            ));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(route.get("airline").get("code")), cb.asc(route.get("number")));

        return em.createQuery(query)
                .setMaxResults(filter.limit() != null ? filter.limit() : 200)
                .getResultList();
    }
}
