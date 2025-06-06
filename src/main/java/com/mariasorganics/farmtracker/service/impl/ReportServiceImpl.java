package com.mariasorganics.farmtracker.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.entity.SalesEntry;
import com.mariasorganics.farmtracker.enums.ExpenseType;
import com.mariasorganics.farmtracker.repository.ExpenseEntryRepository;
import com.mariasorganics.farmtracker.repository.InventoryEntryRepository;
import com.mariasorganics.farmtracker.repository.SalesEntryRepository;
import com.mariasorganics.farmtracker.service.IReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final SalesEntryRepository salesRepo;
    private final ExpenseEntryRepository expenseRepo;
    private final InventoryEntryRepository inventoryRepo;

    @Override
    public Map<String, Double> getTotalSalesAndExpenses(LocalDate start, LocalDate end) {
        double sales = salesRepo.findBySaleDateBetween(start, end).stream().mapToDouble(SalesEntry::getTotalAmount)
                .sum();
        double expenses = expenseRepo.findByExpenseDateBetween(start, end).stream().mapToDouble(ExpenseEntry::getAmount)
                .sum();
        return Map.of("Total Sales", sales, "Total Expenses", expenses);
    }

    @Override
    public Map<String, Double> getCapexAndOpexTotals(LocalDate start, LocalDate end) {
        List<ExpenseEntry> expenses = expenseRepo.findByExpenseDateBetween(start, end);
        double capex = expenses.stream().filter(e -> e.getType() == ExpenseType.CAPEX)
                .mapToDouble(ExpenseEntry::getAmount).sum();
        double opex = expenses.stream().filter(e -> e.getType() == ExpenseType.OPEX)
                .mapToDouble(ExpenseEntry::getAmount).sum();
        return Map.of("CAPEX", capex, "OPEX", opex);
    }

    @Override
    public List<Map<String, Object>> getSalesLog(LocalDate start, LocalDate end) {
        return salesRepo.findBySaleDateBetween(start, end).stream()
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Product", s.getProduct().getName());
                    map.put("Batch", s.getBatchCode());
                    map.put("Qty", s.getQuantitySold());
                    map.put("Unit", s.getUnit());
                    map.put("Total", s.getTotalAmount());
                    map.put("Date", s.getSaleDate());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getExpenseLog(LocalDate start, LocalDate end) {
        return expenseRepo.findByExpenseDateBetween(start, end).stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Description", e.getDescription());
                    map.put("Category", e.getCategory());
                    map.put("Type", e.getType());
                    map.put("Amount", e.getAmount());
                    map.put("Date", e.getExpenseDate());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> getCurrentInventorySnapshot() {
        return inventoryRepo.findAll().stream().collect(Collectors.groupingBy(
                i -> i.getProduct().getName(),
                Collectors.summingDouble(InventoryEntry::getQuantity)));
    }

    @Override
    public double getNetProfit(LocalDate start, LocalDate end) {
        double sales = salesRepo.findBySaleDateBetween(start, end).stream().mapToDouble(SalesEntry::getTotalAmount)
                .sum();
        double expenses = expenseRepo.findByExpenseDateBetween(start, end).stream().mapToDouble(ExpenseEntry::getAmount)
                .sum();
        return sales - expenses;
    }

    @Override
    public Map<String, Double> getProductWiseSales(LocalDate start, LocalDate end) {
        return salesRepo.findBySaleDateBetween(start, end).stream()
                .collect(Collectors.groupingBy(s -> s.getProduct().getName(),
                        Collectors.summingDouble(SalesEntry::getTotalAmount)));
    }

    @Override
    public Map<String, Double> getCategoryWiseExpenses(LocalDate start, LocalDate end) {
        return expenseRepo.findByExpenseDateBetween(start, end).stream()
                .collect(Collectors.groupingBy(ExpenseEntry::getCategory,
                        Collectors.summingDouble(ExpenseEntry::getAmount)));
    }

    @Override
    public Map<String, Object> getConsolidatedReport(LocalDate start, LocalDate end) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.putAll(getTotalSalesAndExpenses(start, end));
        report.put("Net Profit", getNetProfit(start, end));
        report.put("Product-wise Sales", getProductWiseSales(start, end));
        report.put("Category-wise Expenses", getCategoryWiseExpenses(start, end));
        report.put("CAPEX vs OPEX", getCapexAndOpexTotals(start, end));
        return report;
    }
}
