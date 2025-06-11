package com.mariasorganics.farmtracker.service.impl.helpers;
import com.mariasorganics.farmtracker.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("unit")), like)
            );
        };
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) ->
            category == null || category.isBlank() ? null : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }
}
