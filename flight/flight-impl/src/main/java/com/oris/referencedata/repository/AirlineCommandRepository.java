package com.oris.referencedata.repository;

import com.oris.referencedata.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineCommandRepository extends JpaRepository<Airline, String>, JpaSpecificationExecutor<Airline> {
}
