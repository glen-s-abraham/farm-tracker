package com.mariasorganics.farmtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;

public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Long> {
    List<ExpenseEntry> findAllByOrderByExpenseDateDesc();
}
