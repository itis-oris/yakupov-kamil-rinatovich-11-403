package com.arsaka.referencedata.repository;

import com.arsaka.referencedata.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCommandRepository extends JpaRepository<Country, String> {
}
