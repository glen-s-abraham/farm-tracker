package com.mariasorganics.farmtracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mariasorganics.farmtracker.entity.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
    Optional<AppUser> findByUsername(String username);
}
