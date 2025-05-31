package com.mariasorganics.farmtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.service.IExpenseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final IExpenseService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("expenses", service.getAll());
        return "expenses/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("entry", new ExpenseEntry());
        return "expenses/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("entry", service.getById(id));
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
