package com.oris.referencedata.repository;

import com.oris.referencedata.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteCommandRepository extends JpaRepository<Route, UUID> {
}
