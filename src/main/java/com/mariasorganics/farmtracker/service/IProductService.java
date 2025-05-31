package com.mariasorganics.farmtracker.service;

import java.util.List;


import com.mariasorganics.farmtracker.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();
    Product getById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}

