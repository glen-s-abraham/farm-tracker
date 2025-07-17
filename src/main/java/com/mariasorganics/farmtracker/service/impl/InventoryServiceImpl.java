// Source code is decompiled from a .class file using FernFlower decompiler.
package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.Cycle;
import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.CycleRepository;
import com.mariasorganics.farmtracker.repository.InventoryEntryRepository;
import com.mariasorganics.farmtracker.service.IInventoryService;
import com.mariasorganics.farmtracker.service.impl.helpers.InventorySpecifications;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements IInventoryService {
   private final InventoryEntryRepository inventoryEntryRepository;
   private final CycleRepository cycleRepository;

   public InventoryServiceImpl(InventoryEntryRepository inventoryEntryRepository, CycleRepository cycleRepository) {
      this.inventoryEntryRepository = inventoryEntryRepository;
      this.cycleRepository = cycleRepository;
   }

   public InventoryEntry create(InventoryEntry entry) {
      if (entry.getCycleId() == null) {
         throw new IllegalArgumentException("Cycle must be selected.");
      } else {
         Cycle cycle = (Cycle)this.cycleRepository.findById(entry.getCycleId()).orElseThrow(() -> {
            return new RuntimeException("Cycle not found");
         });
         entry.setCycle(cycle);
         int lastHarvestCount = (Integer)this.inventoryEntryRepository.findByCycle_IdOrderByHarvestCountDesc(cycle.getId()).stream().findFirst().map(InventoryEntry::getHarvestCount).orElse(1);
         int newHarvestCount = lastHarvestCount + 1;
         entry.setHarvestCount(newHarvestCount);
         String var10000 = cycle.getName();
         String batchCode = var10000 + "-" + newHarvestCount;
         entry.setBatchCode(batchCode);
         return (InventoryEntry)this.inventoryEntryRepository.save(entry);
      }
   }

   public List<InventoryEntry> getAll() {
      return this.inventoryEntryRepository.findAll();
   }

   public InventoryEntry getById(Long id) {
      return (InventoryEntry)this.inventoryEntryRepository.findById(id).orElseThrow(() -> {
         return new ResourceNotFoundException("InventoryEntry not found with ID: " + id);
      });
   }

   public InventoryEntry update(Long id, InventoryEntry updatedEntry) {
      return (InventoryEntry)this.inventoryEntryRepository.findById(id).map((existing) -> {
         Long oldCycleId = existing.getCycle() != null ? existing.getCycle().getId() : null;
         Long newCycleId = updatedEntry.getCycleId();
         if (newCycleId != null && !newCycleId.equals(oldCycleId)) {
            Cycle newCycle = (Cycle)this.cycleRepository.findById(newCycleId).orElseThrow(() -> {
               return new RuntimeException("Cycle not found");
            });
            existing.setCycle(newCycle);
            int lastHarvestCount = (Integer)this.inventoryEntryRepository.findByCycle_IdOrderByHarvestCountDesc(newCycleId).stream().findFirst().map(InventoryEntry::getHarvestCount).orElse(0);
            int newHarvestCount = lastHarvestCount + 1;
            existing.setHarvestCount(newHarvestCount);
            String var10001 = newCycle.getName();
            existing.setBatchCode(var10001 + "-" + newHarvestCount);
         }

         existing.setProduct(updatedEntry.getProduct());
         existing.setQuantity(updatedEntry.getQuantity());
         existing.setUnit(updatedEntry.getUnit());
         existing.setEntryDate(updatedEntry.getEntryDate());
         existing.setExpiryDate(updatedEntry.getExpiryDate());
         existing.setRemarks(updatedEntry.getRemarks());
         return (InventoryEntry)this.inventoryEntryRepository.save(existing);
      }).orElseThrow(() -> {
         return new RuntimeException("Inventory entry not found with id: " + id);
      });
   }

   public void delete(Long id) {
      if (!this.inventoryEntryRepository.existsById(id)) {
         throw new RuntimeException("Inventory entry not found with id: " + id);
      } else {
         this.inventoryEntryRepository.deleteById(id);
      }
   }

   public List<InventoryEntry> findBatchesByProduct(Long productId) {
      return this.inventoryEntryRepository.findByProduct_IdOrderByEntryDateDesc(productId);
   }

   public Page<InventoryEntry> getPaginated(int page, int size) {
      Pageable pageable = PageRequest.of(page, size, Sort.by(new String[]{"id"}).descending());
      return this.inventoryEntryRepository.findAll(pageable);
   }

   public Page<InventoryEntry> getFilteredPaginated(String keyword, String sortField, String sortDir, LocalDate entryFrom, LocalDate entryTo, LocalDate expiryFrom, LocalDate expiryTo, int page, int size) {
      Specification<InventoryEntry> spec = Specification.where(InventorySpecifications.hasKeyword(keyword)).and(InventorySpecifications.entryDateBetween(entryFrom, entryTo)).and(InventorySpecifications.expiryDateBetween(expiryFrom, expiryTo));
      Sort sort = Sort.by(new String[]{sortField != null ? sortField : "id"});
      sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
      Pageable pageable = PageRequest.of(page, size, sort);
      return this.inventoryEntryRepository.findAll(spec, pageable);
   }
}
