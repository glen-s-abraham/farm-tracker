package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.service.IReportService;
import com.mariasorganics.farmtracker.utils.ExcelReportGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports/export")
@RequiredArgsConstructor
public class ReportExportController {

        private final IReportService reportService;

        @GetMapping("/sales-log")
        public void exportSalesLog(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcel("Sales Log", reportService.getSalesLog(start, end), response,
                                "sales_log.xlsx");
        }

        @GetMapping("/expense-log")
        public void exportExpenseLog(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcel("Expense Log", reportService.getExpenseLog(start, end), response,
                                "expense_log.xlsx");
        }

        @GetMapping("/sales-expense-summary")
        public void exportSalesAndExpenseSummary(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Sales & Expense Summary",
                                reportService.getTotalSalesAndExpenses(start, end), response,
                                "sales_expense_summary.xlsx");
        }

        @GetMapping("/capex-opex-summary")
        public void exportCapexAndOpexSummary(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("CAPEX vs OPEX",
                                reportService.getCapexAndOpexTotals(start, end), response, "capex_opex_summary.xlsx");
        }

        @GetMapping("/net-profit")
        public void exportNetProfit(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Net Profit",
                                Map.of("Net Profit", reportService.getNetProfit(start, end)), response,
                                "net_profit.xlsx");
        }

        @GetMapping("/product-wise-sales")
        public void exportProductWiseSales(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Product-wise Sales",
                                reportService.getProductWiseSales(start, end), response, "product_sales_summary.xlsx");
        }

        @GetMapping("/category-wise-expenses")
        public void exportCategoryWiseExpenses(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Category-wise Expenses",
                                reportService.getCategoryWiseExpenses(start, end), response,
                                "category_expense_summary.xlsx");
        }

        @GetMapping("/inventory")
        public void exportCurrentInventory(HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Inventory Snapshot",
                                reportService.getCurrentInventorySnapshot(), response, "inventory_snapshot.xlsx");
        }

        @GetMapping("/consolidated")
        public void exportConsolidatedReport(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                        HttpServletResponse response) throws IOException {
                ExcelReportGenerator.generateExcelFromMap("Consolidated Report",
                                reportService.getConsolidatedReport(start, end), response, "consolidated_report.xlsx");
        }
}
