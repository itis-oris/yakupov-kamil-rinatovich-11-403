package com.oris.referencedata.repository;

import com.oris.referencedata.model.AirplaneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneTypeCommandRepository extends JpaRepository<AirplaneType, String> {
}
