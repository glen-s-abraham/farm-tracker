package com.mariasorganics.farmtracker.dtos.dashboard;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryMetrics {
    private Map<String, Double> currentInventoryPerProduct;
    private Map<String, Double> inventoryTurnoverPerProduct;
    private List<InventoryControlPoint> inventoryControlChart; // For graphing
}
