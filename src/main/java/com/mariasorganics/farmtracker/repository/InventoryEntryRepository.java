package com.mariasorganics.farmtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mariasorganics.farmtracker.entity.InventoryEntry;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long> {

}
