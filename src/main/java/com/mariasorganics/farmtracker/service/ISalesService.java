package com.mariasorganics.farmtracker.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mariasorganics.farmtracker.entity.SalesEntry;

public interface ISalesService {
    List<SalesEntry> getAllSales();
    SalesEntry getById(Long id);
    SalesEntry save(SalesEntry salesEntry);
    void deleteById(Long id);
    Page<SalesEntry> getPaginated(int page, int size);
}
