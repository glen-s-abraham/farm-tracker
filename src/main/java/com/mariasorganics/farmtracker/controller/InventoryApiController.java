package com.mariasorganics.farmtracker.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mariasorganics.farmtracker.service.IInventoryService;

@RestController // or @Controller + @ResponseBody on method
@RequestMapping("/api/inventory")
public class InventoryApiController {

    private final IInventoryService inventoryService;

    public InventoryApiController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/batches")
    public List<Map<String, Object>> getBatchesForProduct(@RequestParam Long productId) {
        return inventoryService.findBatchesByProduct(productId).stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", entry.getId());
                    map.put("batchCode", entry.getBatchCode());
                    map.put("availableQuantity", entry.getQuantity());
                    map.put("entryDate", entry.getEntryDate()); // Include entryDate for sorting
                    return map;
                })
                .sorted((a, b) -> {
                    LocalDate dateA = (LocalDate) a.get("entryDate");
                    LocalDate dateB = (LocalDate) b.get("entryDate");
                    return dateB.compareTo(dateA); // Descending order
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/latest-batch")
    public Map<String, Object> getLatestBatchForProduct(@RequestParam Long productId) {
        return inventoryService.findBatchesByProduct(productId).stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId())) // latest = highest ID
                .findFirst()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("batchCode", entry.getBatchCode());
                    map.put("quantity", entry.getQuantity());
                    map.put("entryDate", entry.getEntryDate());
                    return map;
                })
                .orElse(null);
    }

}
