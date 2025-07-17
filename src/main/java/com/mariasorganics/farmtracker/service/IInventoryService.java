// Source code is decompiled from a .class file using FernFlower decompiler.
package com.mariasorganics.farmtracker.service;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IInventoryService {
   InventoryEntry create(InventoryEntry entry);

   List<InventoryEntry> getAll();

   InventoryEntry getById(Long id);

   InventoryEntry update(Long id, InventoryEntry updatedEntry);

   void delete(Long id);

   List<InventoryEntry> findBatchesByProduct(Long productId);

   Page<InventoryEntry> getPaginated(int page, int size);

   Page<InventoryEntry> getFilteredPaginated(String keyword, String sortField, String sortDir, LocalDate entryFrom, LocalDate entryTo, LocalDate expiryFrom, LocalDate expiryTo, int page, int size);
}
