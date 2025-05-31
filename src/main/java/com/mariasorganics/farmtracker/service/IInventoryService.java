package com.mariasorganics.farmtracker.service;

import java.util.List;

import com.mariasorganics.farmtracker.entity.InventoryEntry;

public interface IInventoryService {
    InventoryEntry save(InventoryEntry entry);

    List<InventoryEntry> getAll();

    InventoryEntry getById(Long id);

    InventoryEntry update(Long id, InventoryEntry updatedEntry);

    void delete(Long id);
}
