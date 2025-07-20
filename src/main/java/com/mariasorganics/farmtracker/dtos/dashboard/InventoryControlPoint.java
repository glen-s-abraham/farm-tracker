package com.mariasorganics.farmtracker.dtos.dashboard;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryControlPoint {
    private LocalDate date;
    private String productName;
    private double totalQuantity;
}
