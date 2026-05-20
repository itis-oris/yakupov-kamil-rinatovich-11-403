package com.arsaka.referencedata.repository;

import com.arsaka.referencedata.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<City, String> {
}
