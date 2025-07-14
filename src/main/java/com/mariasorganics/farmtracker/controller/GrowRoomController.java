package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.entity.GrowRoom;
import com.mariasorganics.farmtracker.service.IGrowRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grow-rooms")
public class GrowRoomController {

    private final IGrowRoomService service;

    public GrowRoomController(IGrowRoomService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", service.getAll());
        return "growroom/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("room", new GrowRoom());
        return "growroom/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute GrowRoom room) {
        service.save(room);
        return "redirect:/grow-rooms";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("room", service.getById(id));
        return "growroom/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/grow-rooms";
    }
}
