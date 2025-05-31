package com.mariasorganics.farmtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mariasorganics.farmtracker.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
