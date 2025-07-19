package com.mariasorganics.farmtracker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cycle> cycles = new ArrayList();
}

