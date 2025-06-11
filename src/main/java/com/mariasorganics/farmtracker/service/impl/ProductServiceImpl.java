package com.mariasorganics.farmtracker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mariasorganics.farmtracker.entity.Product;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.ProductRepository;
import com.mariasorganics.farmtracker.service.IProductService;
import static com.mariasorganics.farmtracker.service.impl.helpers.ProductSpecifications.*;

import lombok.AllArgsConstructor;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Override
    public Product save(Product product) {
        return repo.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = getById(id); // Ensures existence
        repo.delete(product);
    }

    @Override
    public Page<Product> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repo.findAll(pageable);
    }

    @Override
    public Page<Product> getFilteredPaginated(
            String keyword,
            String category,
            String sortField,
            String sortDir,
            int page,
            int size) {

        Specification<Product> spec = Specification.where(hasKeyword(keyword)).and(hasCategory(category));

        Sort sort = Sort.by(sortField != null ? sortField : "id");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(spec, pageable);
    }
}