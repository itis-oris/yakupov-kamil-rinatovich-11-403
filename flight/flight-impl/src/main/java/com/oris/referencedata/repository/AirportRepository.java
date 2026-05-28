package com.oris.referencedata.repository;

import com.oris.referencedata.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<City, String> {
}
