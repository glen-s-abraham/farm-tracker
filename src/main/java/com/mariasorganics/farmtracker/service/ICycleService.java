package com.mariasorganics.farmtracker.service;

import com.mariasorganics.farmtracker.entity.Cycle;

import java.util.List;

public interface ICycleService {
    List<Cycle> getAll();
    Cycle getById(Long id);
    Cycle save(Cycle cycle);
    void delete(Long id);
    String generateCycleName(Long growRoomId, String growRoomName, java.time.LocalDate startDate);
}
