package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.InventoryEntryRepository;
import com.mariasorganics.farmtracker.service.IInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryEntryRepository inventoryEntryRepository;

    public InventoryServiceImpl(InventoryEntryRepository inventoryEntryRepository) {
        this.inventoryEntryRepository = inventoryEntryRepository;
    }

    @Override
    public InventoryEntry save(InventoryEntry entry) {
        return inventoryEntryRepository.save(entry);
    }

    @Override
    public List<InventoryEntry> getAll() {
        return inventoryEntryRepository.findAll();
    }

    @Override
    public InventoryEntry getById(Long id) {
        return inventoryEntryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("InventoryEntry not found with ID: " + id));
    }

    @Override
    public InventoryEntry update(Long id, InventoryEntry updatedEntry) {
        return inventoryEntryRepository.findById(id)
                .map(existing -> {
                    existing.setBatchCode(updatedEntry.getBatchCode());
                    existing.setProduct(updatedEntry.getProduct());
                    existing.setQuantity(updatedEntry.getQuantity());
                    existing.setUnit(updatedEntry.getUnit());
                    existing.setEntryDate(updatedEntry.getEntryDate());
                    existing.setExpiryDate(updatedEntry.getExpiryDate());
                    existing.setRemarks(updatedEntry.getRemarks());
                    return inventoryEntryRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Inventory entry not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!inventoryEntryRepository.existsById(id)) {
            throw new RuntimeException("Inventory entry not found with id: " + id);
        }
        inventoryEntryRepository.deleteById(id);
    }
}
