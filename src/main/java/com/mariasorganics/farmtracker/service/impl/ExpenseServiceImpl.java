package com.mariasorganics.farmtracker.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.ExpenseEntryRepository;
import com.mariasorganics.farmtracker.service.IExpenseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements IExpenseService {

    private final ExpenseEntryRepository repo;

    @Override
    public List<ExpenseEntry> getAll() {
        return repo.findAllByOrderByExpenseDateDesc();
    }

    @Override
    public ExpenseEntry getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    @Override
    public ExpenseEntry save(ExpenseEntry entry) {
        return repo.save(entry);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
