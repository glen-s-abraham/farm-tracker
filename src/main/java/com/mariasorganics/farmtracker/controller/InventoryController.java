package com.mariasorganics.farmtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import com.mariasorganics.farmtracker.entity.Product;
import com.mariasorganics.farmtracker.service.IInventoryService;
import com.mariasorganics.farmtracker.service.IProductService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final IInventoryService inventoryService;
    private final IProductService productService;

    public InventoryController(IInventoryService inventoryService, IProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inventoryEntries", inventoryService.getAll());
        return "inventory/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entry", new InventoryEntry());
        model.addAttribute("products", productService.getAllProducts());
        return "inventory/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute InventoryEntry entry) {
        Product product = productService.getById(entry.getProductId());
        entry.setProduct(product);
        inventoryService.save(entry);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("entry", inventoryService.getById(id));
        model.addAttribute("products", productService.getAllProducts());
        return "inventory/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return "redirect:/inventory";
    }
}
