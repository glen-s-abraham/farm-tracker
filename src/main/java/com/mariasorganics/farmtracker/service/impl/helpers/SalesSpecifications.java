package com.mariasorganics.farmtracker.service.impl.helpers;


import com.mariasorganics.farmtracker.entity.SalesEntry;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SalesSpecifications {

    public static Specification<SalesEntry> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("batchCode")), like),
                cb.like(cb.lower(root.get("customerName")), like),
                cb.like(cb.lower(root.get("remarks")), like),
                cb.like(cb.lower(root.get("product").get("name")), like)
            );
        };
    }

    public static Specification<SalesEntry> saleDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("saleDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("saleDate"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("saleDate"), to);
            }
            return null;
        };
    }
}
