package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.entity.Product;
import com.mariasorganics.farmtracker.entity.SalesEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.service.IInventoryService;
import com.mariasorganics.farmtracker.service.IProductService;
import com.mariasorganics.farmtracker.service.ISalesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sales")
public class SalesController {

    private final ISalesService salesService;
    private final IProductService productService;

    public SalesController(ISalesService salesService, IProductService productService) {
        this.salesService = salesService;
        this.productService = productService;
    }

    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("salesList", salesService.getAllSales());
        return "sales/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("entry", new SalesEntry()); // ensure 'entry' is never null
        model.addAttribute("products", productService.getAllProducts());
        return "sales/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        SalesEntry entry = salesService.getById(id);
        if (entry == null) {
            throw new ResourceNotFoundException("Sale entry not found with ID: " + id);
        }
        model.addAttribute("entry", entry);
        model.addAttribute("products", productService.getAllProducts());
        return "sales/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute SalesEntry entry) {
        // defensive null check
        if (entry.getProductId() == null) {
            throw new IllegalArgumentException("Product must be selected");
        }

        Product product = productService.getById(entry.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product not found for ID: " + entry.getProductId());
        }

        entry.setProduct(product);
        salesService.save(entry);
        return "redirect:/sales";
    }

    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        salesService.deleteById(id);
        return "redirect:/sales";
    }
}
