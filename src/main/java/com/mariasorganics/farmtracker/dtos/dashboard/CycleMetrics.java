package com.mariasorganics.farmtracker.dtos.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CycleMetrics {
    private List<CycleYield> yields;
}
