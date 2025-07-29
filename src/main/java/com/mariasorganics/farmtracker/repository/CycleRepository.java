package com.mariasorganics.farmtracker.repository;

import com.mariasorganics.farmtracker.entity.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
    List<Cycle> findByGrowRoom_IdOrderByStartDateDesc(Long growRoomId);
    boolean existsByName(String name);
    boolean existsByGrowRoom_IdAndStatus(Long growRoomId, Cycle.Status status);
}
