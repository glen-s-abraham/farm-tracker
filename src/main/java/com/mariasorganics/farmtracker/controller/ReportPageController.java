package com.mariasorganics.farmtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportPageController {

    @GetMapping
    public String showReportsPage(Model model) {
        // You can pass defaults or recent dates here if needed
        return "reports/reports";
    }
} 
