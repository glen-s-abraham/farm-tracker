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
public class SalesMetrics {
    private double totalSalesValue;
    private Map<String, Double> salesByProduct;
    private Map<String, Double> averagePricePerProduct;
}
