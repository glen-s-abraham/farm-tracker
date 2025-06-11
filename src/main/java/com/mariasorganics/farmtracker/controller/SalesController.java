package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.entity.Product;
import com.mariasorganics.farmtracker.entity.SalesEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.service.IInventoryService;
import com.mariasorganics.farmtracker.service.IProductService;
import com.mariasorganics.farmtracker.service.ISalesService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate saleFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate saleTo,
            Model model) {

        Page<SalesEntry> salesEntryPage = salesService.getFilteredPaginated(
                keyword, saleFrom, saleTo, sortField, sortDir, page, size);

        model.addAttribute("salesEntryPage", salesEntryPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("saleFrom", saleFrom);
        model.addAttribute("saleTo", saleTo);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
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
