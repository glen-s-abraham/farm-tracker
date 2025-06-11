package com.mariasorganics.farmtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mariasorganics.farmtracker.entity.InventoryEntry;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long>, JpaSpecificationExecutor<InventoryEntry> {

    List<InventoryEntry> findByProduct_IdOrderByEntryDateDesc(Long productId);

    Optional<InventoryEntry> findByProduct_IdAndBatchCode(Long productId, String batchCode);
}
