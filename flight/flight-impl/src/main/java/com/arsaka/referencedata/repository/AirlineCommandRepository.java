package com.arsaka.referencedata.repository;

import com.arsaka.referencedata.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineCommandRepository extends JpaRepository<Airline, String>, JpaSpecificationExecutor<Airline> {
}
