package com.mariasorganics.farmtracker.service;

import java.time.LocalDate;

import com.mariasorganics.farmtracker.dtos.dashboard.DashboardSummary;

public interface IDashboardService {
    DashboardSummary getDashboardSummary(LocalDate from, LocalDate to);
}
