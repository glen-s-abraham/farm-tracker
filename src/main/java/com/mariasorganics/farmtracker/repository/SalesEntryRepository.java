package com.mariasorganics.farmtracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mariasorganics.farmtracker.entity.SalesEntry;

@Repository
public interface SalesEntryRepository extends JpaRepository<SalesEntry, Long>, JpaSpecificationExecutor<SalesEntry> {
    List<SalesEntry> findBySaleDateBetween(LocalDate start, LocalDate end);
}
