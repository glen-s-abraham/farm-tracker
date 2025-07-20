package com.mariasorganics.farmtracker.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CycleYield {
    private String cycleName;
    private double totalHarvested;
    private double totalExpense;
    private double costPerUnit; // ₹ per kg or unit
}
