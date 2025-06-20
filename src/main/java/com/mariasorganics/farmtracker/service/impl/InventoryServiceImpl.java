package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.InventoryEntryRepository;
import com.mariasorganics.farmtracker.service.IInventoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.mariasorganics.farmtracker.service.impl.helpers.InventorySpecifications.*;

import java.time.LocalDate;
import java.util.List;

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
        return inventoryEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryEntry not found with ID: " + id));
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

    @Override
    public List<InventoryEntry> findBatchesByProduct(Long productId) {
        return inventoryEntryRepository.findByProduct_IdOrderByEntryDateDesc(productId);
    }

    @Override
    public Page<InventoryEntry> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return inventoryEntryRepository.findAll(pageable);
    }

    @Override
    public Page<InventoryEntry> getFilteredPaginated(String keyword, String sortField, String sortDir,
            LocalDate entryFrom, LocalDate entryTo,
            LocalDate expiryFrom, LocalDate expiryTo,
            int page, int size) {

        Specification<InventoryEntry> spec = Specification.where(hasKeyword(keyword))
                .and(entryDateBetween(entryFrom, entryTo))
                .and(expiryDateBetween(expiryFrom, expiryTo));

        Sort sort = Sort.by(sortField != null ? sortField : "id");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return inventoryEntryRepository.findAll(spec, pageable);
    }
}
