package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.dtos.dashboard.*;
import com.mariasorganics.farmtracker.entity.*;
import com.mariasorganics.farmtracker.enums.ExpenseType;
import com.mariasorganics.farmtracker.repository.*;
import com.mariasorganics.farmtracker.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

        private final InventoryEntryRepository inventoryRepo;
        private final SalesEntryRepository salesRepo;
        private final ExpenseEntryRepository expenseRepo;
        private final ProductRepository productRepo;
        private final CycleRepository cycleRepo;

        @Override
        public DashboardSummary getDashboardSummary(LocalDate from, LocalDate to) {
                DashboardSummary summary = new DashboardSummary();
                summary.setInventory(getInventoryMetrics(from, to));
                summary.setSales(getSalesMetrics(from, to));
                summary.setExpenses(getExpenseMetrics(from, to));
                summary.setCycles(getCycleMetrics(from, to));
                return summary;
        }

        private InventoryMetrics getInventoryMetrics(LocalDate from, LocalDate to) {
                List<Product> products = productRepo.findAll();
                Map<String, Double> currentInventory = new HashMap<>();
                Map<String, Double> turnover = new HashMap<>();

                for (Product product : products) {
                        List<InventoryEntry> inventoryEntries = inventoryRepo
                                        .findByProduct_IdOrderByEntryDateDesc(product.getId());
                        double currentQty = inventoryEntries.stream().mapToDouble(InventoryEntry::getQuantity).sum();
                        currentInventory.put(product.getName(), currentQty);

                        double totalSold = salesRepo.findBySaleDateBetween(from, to).stream()
                                        .filter(s -> s.getProduct().getId().equals(product.getId()))
                                        .mapToDouble(SalesEntry::getQuantitySold)
                                        .sum();

                        double opening = inventoryEntries.stream().filter(e -> !e.getEntryDate().isAfter(from))
                                        .mapToDouble(InventoryEntry::getQuantity).sum();
                        double closing = inventoryEntries.stream().filter(e -> !e.getEntryDate().isAfter(to))
                                        .mapToDouble(InventoryEntry::getQuantity).sum();
                        double avg = (opening + closing) / 2.0;

                        turnover.put(product.getName(), avg > 0 ? totalSold / avg : 0);
                }

                return new InventoryMetrics(currentInventory, turnover, Collections.emptyList());
        }

        private SalesMetrics getSalesMetrics(LocalDate from, LocalDate to) {
                List<SalesEntry> sales = salesRepo.findBySaleDateBetween(from, to);
                sales.forEach(s -> System.out.println("Product: " + s.getProduct()));

                double total = sales.stream().mapToDouble(SalesEntry::getTotalAmount).sum();

                Map<String, Double> byProduct = sales.stream()
                                .collect(Collectors.groupingBy(s -> s.getProduct().getName(),
                                                Collectors.summingDouble(SalesEntry::getQuantitySold)));

                Map<String, Double> avgPrice = sales.stream()
                                .collect(Collectors.groupingBy(s -> s.getProduct().getName(),
                                                Collectors.averagingDouble(SalesEntry::getPricePerUnit)));

                return new SalesMetrics(total, byProduct, avgPrice);
        }

        private ExpenseMetrics getExpenseMetrics(LocalDate from, LocalDate to) {
                List<ExpenseEntry> expenses = expenseRepo.findByExpenseDateBetween(from, to);
                double total = expenses.stream().mapToDouble(ExpenseEntry::getAmount).sum();

                Map<String, Double> byCategory = expenses.stream()
                                .collect(Collectors.groupingBy(e -> e.getCategory().name(),
                                                Collectors.summingDouble(ExpenseEntry::getAmount)));

                double capex = expenses.stream()
                                .filter(e -> e.getType() == ExpenseType.CAPEX)
                                .mapToDouble(ExpenseEntry::getAmount).sum();

                double opex = expenses.stream()
                                .filter(e -> e.getType() == ExpenseType.OPEX)
                                .mapToDouble(ExpenseEntry::getAmount).sum();

                return new ExpenseMetrics(total, byCategory, capex, opex);
        }

        private CycleMetrics getCycleMetrics(LocalDate from, LocalDate to) {
                List<Cycle> cycles = cycleRepo.findAll();
                List<CycleYield> yields = new ArrayList<>();

                for (Cycle c : cycles) {
                        // Inventory: entries from this cycle within date range
                        double inventoryQty = c.getInventoryEntries().stream()
                                        .mapToDouble(InventoryEntry::getQuantity)
                                        .sum();

                        // Sales: match entries whose batchCode starts with this cycle name
                        double salesQty = salesRepo.findBySaleDateBetween(from, to).stream()
                                        .filter(s -> s.getBatchCode() != null
                                                        && s.getBatchCode().startsWith(c.getName()))
                                        .mapToDouble(SalesEntry::getQuantitySold)
                                        .sum();

                        double harvested = inventoryQty + salesQty;

                        double cost = c.getExpenses().stream()
                                        .mapToDouble(ExpenseEntry::getAmount)
                                        .sum();

                        double costPerUnit = harvested > 0 ? cost / harvested : 0;

                        yields.add(new CycleYield(c.getName(), harvested, cost, costPerUnit));
                }

                return new CycleMetrics(yields);
        }

}
