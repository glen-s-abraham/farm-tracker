package com.mariasorganics.farmtracker.controller;

import java.time.LocalDate;

import com.mariasorganics.farmtracker.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboardView(
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            Model model) {
        if (from == null)
            from = LocalDate.now().minusDays(30);
        if (to == null)
            to = LocalDate.now();

        model.addAttribute("dashboard", dashboardService.getDashboardSummary(from, to));
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "dashboard/index";
    }

}
