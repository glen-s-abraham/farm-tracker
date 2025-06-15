package com.mariasorganics.farmtracker.entity;

import java.time.LocalDate;

import com.mariasorganics.farmtracker.enums.ExpenseType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEntry extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String category; // e.g., Packaging, Transport, Fertilizer

    private double amount;

    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseType type; // CAPEX or OPEX

    private String remarks;
}
