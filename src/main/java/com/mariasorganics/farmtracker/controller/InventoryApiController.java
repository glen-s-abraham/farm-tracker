package com.mariasorganics.farmtracker.controller;

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
                    map.put("batchCode", entry.getBatchCode());
                    map.put("availableQuantity", entry.getQuantity());
                    return map;
                })
                .sorted((a, b) -> ((String) b.get("batchCode")).compareTo((String) a.get("batchCode")))
                .collect(Collectors.toList());
    }
}
