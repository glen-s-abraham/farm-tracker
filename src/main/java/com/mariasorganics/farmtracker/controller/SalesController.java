package com.mariasorganics.farmtracker.controller;

import com.mariasorganics.farmtracker.entity.Cycle;
import com.mariasorganics.farmtracker.entity.Product;
import com.mariasorganics.farmtracker.entity.SalesEntry;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.service.ICycleService;
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
    private final ICycleService cycleService;

    public SalesController(ISalesService salesService, IProductService productService, ICycleService cycleService) {
        this.salesService = salesService;
        this.productService = productService;
        this.cycleService = cycleService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long cycleId,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate saleFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate saleTo,
            Model model) {

        Page<SalesEntry> salesEntryPage = salesService.getFilteredPaginated(
                keyword, cycleId, saleFrom, saleTo, sortField, sortDir, page, size);

        model.addAttribute("salesEntryPage", salesEntryPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("saleFrom", saleFrom);
        model.addAttribute("saleTo", saleTo);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("cycles", cycleService.getAll());
        model.addAttribute("cycleId", cycleId);

        return "sales/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        SalesEntry entry = new SalesEntry();
        entry.setSaleDate(LocalDate.now());
        entry.setUnit("Packet");
        entry.setPricePerUnit(100);
        model.addAttribute("entry", entry); // ensure 'entry' is never null
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cycles", cycleService.getAll());
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
        model.addAttribute("cycles", cycleService.getAll());
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

        if (entry.getCycle() != null && entry.getCycle().getId() != null) {
            Cycle cycle = cycleService.getById(entry.getCycle().getId());
            entry.setCycle(cycle);
        } else {
            entry.setCycle(null);
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
