package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.entity.Cycle;
import com.mariasorganics.farmtracker.service.ICycleService;
import com.mariasorganics.farmtracker.service.IGrowRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cycles")
public class CycleController {

    private final ICycleService cycleService;
    private final IGrowRoomService growRoomService;

    public CycleController(ICycleService cycleService, IGrowRoomService growRoomService) {
        this.cycleService = cycleService;
        this.growRoomService = growRoomService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cycles", cycleService.getAll());
        return "cycle/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("entry", new Cycle());
        model.addAttribute("rooms", growRoomService.getAll());
        return "cycle/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("entry") Cycle cycle) {
        cycleService.save(cycle);
        return "redirect:/cycles";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("entry", cycleService.getById(id));
        model.addAttribute("rooms", growRoomService.getAll());
        return "cycle/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        cycleService.delete(id);
        return "redirect:/cycles";
    }
}
