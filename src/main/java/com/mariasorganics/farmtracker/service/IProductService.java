package com.mariasorganics.farmtracker.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mariasorganics.farmtracker.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();

    Product getById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    Page<Product> getPaginated(int page, int size);

    Page<Product> getFilteredPaginated(
            String keyword,
            String category,
            String sortField,
            String sortDir,
            int page,
            int size);
}
