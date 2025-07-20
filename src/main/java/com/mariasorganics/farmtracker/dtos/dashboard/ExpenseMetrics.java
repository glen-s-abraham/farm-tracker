package com.mariasorganics.farmtracker.dtos.dashboard;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseMetrics {
    private double totalExpense;
    private Map<String, Double> expenseByCategory;
    private double capex;
    private double opex;
}
