package com.mariasorganics.farmtracker.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grow_room_id", nullable = false)
    private GrowRoom growRoom;

    private LocalDate startDate;

    private String name; // Auto-generated as growRoom.name + "_" + startDate (e.g., "RoomA_2025-07-14")

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        COMPLETED
    }
}
