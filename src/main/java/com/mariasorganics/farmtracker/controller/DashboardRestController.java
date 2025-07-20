package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.dtos.dashboard.DashboardSummary;
import com.mariasorganics.farmtracker.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

    private final IDashboardService dashboardService;

    @GetMapping
    public DashboardSummary getDashboard(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return dashboardService.getDashboardSummary(from, to);
    }
}
