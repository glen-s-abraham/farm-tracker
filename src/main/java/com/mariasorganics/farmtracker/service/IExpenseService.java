package com.mariasorganics.farmtracker.service;

import java.util.List;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;

public interface IExpenseService {
    List<ExpenseEntry> getAll();
    ExpenseEntry getById(Long id);
    ExpenseEntry save(ExpenseEntry entry);
    void deleteById(Long id);
}
