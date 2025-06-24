package com.mariasorganics.farmtracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IReportService {

    Map<String, Double> getTotalSalesAndExpenses(LocalDate start, LocalDate end);

    Map<String, Double> getCapexAndOpexTotals(LocalDate start, LocalDate end);

    List<Map<String, Object>> getSalesLog(LocalDate start, LocalDate end);

    List<Map<String, Object>> getExpenseLog(LocalDate start, LocalDate end);

    Map<String, Double> getCurrentInventorySnapshot();

    double getNetProfit(LocalDate start, LocalDate end);

    Map<String, Double> getProductWiseSales(LocalDate start, LocalDate end);

    Map<String, Double> getCategoryWiseExpenses(LocalDate start, LocalDate end);

    Map<String, Object> getConsolidatedReport(LocalDate start, LocalDate end);

    double getNetSales(LocalDate start, LocalDate end);

    double getTotalProduction(LocalDate start, LocalDate end);

    double getOpex(LocalDate start, LocalDate end);

    double getCapex(LocalDate start, LocalDate end);

}
