package com.arsaka.referencedata.repository;

import com.arsaka.referencedata.model.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirplaneCommandRepository extends JpaRepository<Airplane, UUID>, JpaSpecificationExecutor<Airplane> {
    Optional<Airplane> findByNumber(String number);
}
