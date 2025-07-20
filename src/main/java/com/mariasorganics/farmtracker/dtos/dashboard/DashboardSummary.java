package com.mariasorganics.farmtracker.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DashboardSummary {
    private InventoryMetrics inventory;
    private SalesMetrics sales;
    private ExpenseMetrics expenses;
    private CycleMetrics cycles;
}
