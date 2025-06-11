package com.mariasorganics.farmtracker.service.impl.helpers;

import com.mariasorganics.farmtracker.entity.InventoryEntry;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class InventorySpecifications {

    public static Specification<InventoryEntry> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank())
                return null;
            String likeKeyword = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("batchCode")), likeKeyword),
                    cb.like(cb.lower(root.get("remarks")), likeKeyword),
                    cb.like(cb.lower(root.get("product").get("name")), likeKeyword));
        };
    }

    public static Specification<InventoryEntry> hasProductId(Long productId) {
        return (root, query, cb) -> productId == null ? null : cb.equal(root.get("product").get("id"), productId);
    }

    public static Specification<InventoryEntry> entryDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("entryDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("entryDate"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("entryDate"), to);
            }
            return null;
        };
    }

    public static Specification<InventoryEntry> expiryDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("expiryDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("expiryDate"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("expiryDate"), to);
            }
            return null;
        };
    }
}
