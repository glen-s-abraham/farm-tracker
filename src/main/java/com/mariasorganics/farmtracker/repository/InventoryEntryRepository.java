package com.mariasorganics.farmtracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mariasorganics.farmtracker.entity.InventoryEntry;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long> {
    List<InventoryEntry> findByProduct_IdOrderByEntryDateDesc(Long productId);
    Optional<InventoryEntry> findByProduct_IdAndBatchCode(Long productId, String batchCode);
}
