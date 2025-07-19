package com.mariasorganics.farmtracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.enums.ExpenseType;

public interface IExpenseService {
    List<ExpenseEntry> getAll();

    ExpenseEntry getById(Long id);

    ExpenseEntry save(ExpenseEntry entry);

    void deleteById(Long id);

    Page<ExpenseEntry> getPaginated(int page, int size);

    Page<ExpenseEntry> getFilteredPaginated(String keyword,
            LocalDate expenseFrom, LocalDate expenseTo,
            ExpenseType type, Long cycleId,
            String sortField, String sortDir,
            int page, int size);

}
