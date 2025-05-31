package com.mariasorganics.farmtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mariasorganics.farmtracker.entity.SalesEntry;

@Repository
public interface SalesEntryRepository extends JpaRepository<SalesEntry, Long> {
}
