package com.mariasorganics.farmtracker.service.impl.helpers;

import com.mariasorganics.farmtracker.entity.AppUser;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<AppUser> hasUsernameLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            return cb.like(cb.lower(root.get("username")), "%" + keyword.toLowerCase() + "%");
        };
    }
}
