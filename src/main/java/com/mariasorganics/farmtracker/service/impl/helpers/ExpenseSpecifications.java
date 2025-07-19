package com.mariasorganics.farmtracker.service.impl.helpers;

import com.mariasorganics.farmtracker.entity.ExpenseEntry;
import com.mariasorganics.farmtracker.enums.ExpenseType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExpenseSpecifications {

    public static Specification<ExpenseEntry> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank())
                return null;
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(root.get("category")), like),
                    cb.like(cb.lower(root.get("remarks")), like));
        };
    }

    public static Specification<ExpenseEntry> expenseDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("expenseDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("expenseDate"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("expenseDate"), to);
            }
            return null;
        };
    }

    public static Specification<ExpenseEntry> typeIs(ExpenseType type) {
        return (root, query, cb) -> type != null ? cb.equal(root.get("type"), type) : null;
    }

    public static Specification<ExpenseEntry> cycleIs(Long cycleId) {
        return (root, query, cb) -> cycleId != null ? cb.equal(root.get("cycle").get("id"), cycleId) : null;
    }

}
