package com.mariasorganics.farmtracker.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesEntry extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchCode;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cycle_id", nullable = true)
    private Cycle cycle;

    private double quantitySold;
    private String unit;
    private LocalDate saleDate;
    private double pricePerUnit;
    private double totalAmount;

    private String customerName;
    private String remarks;

    @Transient
    private Long productId;

    @Transient
    private Long cycleId;
}
