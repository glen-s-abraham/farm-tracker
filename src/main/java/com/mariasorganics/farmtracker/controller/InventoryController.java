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
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryTo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryTo,
            Model model) {

        Page<InventoryEntry> inventoriesPage = inventoryService.getFilteredPaginated(
                keyword, sortField, sortDir, entryFrom, entryTo, expiryFrom, expiryTo, page, size);

        model.addAttribute("inventoriesPage", inventoriesPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("entryFrom", entryFrom);
        model.addAttribute("entryTo", entryTo);
        model.addAttribute("expiryFrom", expiryFrom);
        model.addAttribute("expiryTo", expiryTo);

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
