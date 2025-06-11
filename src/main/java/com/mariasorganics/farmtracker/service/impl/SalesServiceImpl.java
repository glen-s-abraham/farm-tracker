package com.mariasorganics.farmtracker.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.entity.SalesEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.InventoryEntryRepository;
import com.mariasorganics.farmtracker.repository.SalesEntryRepository;
import com.mariasorganics.farmtracker.service.ISalesService;
import static com.mariasorganics.farmtracker.service.impl.helpers.SalesSpecifications.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesServiceImpl implements ISalesService {

    private final SalesEntryRepository repo;
    private final InventoryEntryRepository inventoryRepo;

    @Override
    public List<SalesEntry> getAllSales() {
        return repo.findAll();
    }

    @Override
    public SalesEntry getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales entry not found with ID: " + id));
    }

    @Override
    public SalesEntry save(SalesEntry entry) {
        InventoryEntry inventory = inventoryRepo
                .findByProduct_IdAndBatchCode(entry.getProduct().getId(), entry.getBatchCode())
                .orElseThrow(() -> new IllegalArgumentException("Matching inventory not found"));

        double currentInventory = inventory.getQuantity();

        // Always update total amount before saving
        entry.setTotalAmount(entry.getQuantitySold() * entry.getPricePerUnit());

        if (entry.getId() == null) {
            // New Sale
            if (entry.getQuantitySold() > currentInventory) {
                throw new IllegalArgumentException("Not enough inventory to complete the sale.");
            }

            SalesEntry saved = repo.save(entry);
            inventory.setQuantity(currentInventory - entry.getQuantitySold());
            inventoryRepo.save(inventory);

            return saved;

        } else {
            // Edit Sale
            SalesEntry existingSale = repo.findById(entry.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sale entry not found"));

            boolean sameBatch = existingSale.getProduct().getId().equals(entry.getProduct().getId()) &&
                    existingSale.getBatchCode().equals(entry.getBatchCode());

            if (sameBatch) {
                double adjustedAvailable = currentInventory + existingSale.getQuantitySold();

                if (entry.getQuantitySold() > adjustedAvailable) {
                    throw new IllegalArgumentException("Not enough inventory to update the sale.");
                }

                SalesEntry updated = repo.save(entry);
                inventory.setQuantity(adjustedAvailable - entry.getQuantitySold());
                inventoryRepo.save(inventory);
                return updated;

            } else {
                // Revert old batch quantity
                InventoryEntry oldInventory = inventoryRepo
                        .findByProduct_IdAndBatchCode(existingSale.getProduct().getId(), existingSale.getBatchCode())
                        .orElseThrow(() -> new IllegalArgumentException("Old batch inventory not found"));

                oldInventory.setQuantity(oldInventory.getQuantity() + existingSale.getQuantitySold());
                inventoryRepo.save(oldInventory);

                // Check new batch inventory
                if (entry.getQuantitySold() > currentInventory) {
                    throw new IllegalArgumentException("Not enough inventory in new batch.");
                }

                SalesEntry updated = repo.save(entry);
                inventory.setQuantity(currentInventory - entry.getQuantitySold());
                inventoryRepo.save(inventory);

                return updated;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        SalesEntry entry = getById(id);

        // Restore quantity to the batch before deletion
        InventoryEntry inventory = inventoryRepo
                .findByProduct_IdAndBatchCode(entry.getProduct().getId(), entry.getBatchCode())
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for deletion adjustment"));

        inventory.setQuantity(inventory.getQuantity() + entry.getQuantitySold());
        inventoryRepo.save(inventory);

        repo.delete(entry);
    }

    @Override
    public Page<SalesEntry> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repo.findAll(pageable);
    }

    public Page<SalesEntry> getFilteredPaginated(
            String keyword,
            LocalDate saleFrom, LocalDate saleTo,
            String sortField, String sortDir,
            int page, int size) {

        Specification<SalesEntry> spec = Specification.where(hasKeyword(keyword))
                .and(saleDateBetween(saleFrom, saleTo));

        Sort sort = Sort.by(sortField != null ? sortField : "id");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(spec, pageable);
    }
}
