package com.mariasorganics.farmtracker.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.enums.ExpenseType;
import com.mariasorganics.farmtracker.service.ICycleService;
import com.mariasorganics.farmtracker.service.IExpenseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final IExpenseService service;
    private final ICycleService cycleService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expenseFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expenseTo,
            @RequestParam(required = false) ExpenseType type,
            Model model) {

        Page<ExpenseEntry> expensesPage = service.getFilteredPaginated(
                keyword, expenseFrom, expenseTo, type, sortField, sortDir, page, size);

        model.addAttribute("expensesPage", expensesPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("expenseFrom", expenseFrom);
        model.addAttribute("expenseTo", expenseTo);
        model.addAttribute("type", type);

        return "expenses/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        ExpenseEntry entry = new ExpenseEntry();
        entry.setExpenseDate(LocalDate.now());
        model.addAttribute("entry", entry);
         model.addAttribute("cycles", cycleService.getAll());
        return "expenses/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("entry", service.getById(id));
        model.addAttribute("cycles", cycleService.getAll());
        return "expenses/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ExpenseEntry entry) {
        service.save(entry);
        return "redirect:/expenses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/expenses";
    }
}
