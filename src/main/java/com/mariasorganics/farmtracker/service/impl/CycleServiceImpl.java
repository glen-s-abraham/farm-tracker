package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.Cycle;
import com.mariasorganics.farmtracker.entity.GrowRoom;
import com.mariasorganics.farmtracker.repository.CycleRepository;
import com.mariasorganics.farmtracker.repository.GrowRoomRepository;
import com.mariasorganics.farmtracker.service.ICycleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CycleServiceImpl implements ICycleService {

    private final CycleRepository cycleRepository;

    private final GrowRoomRepository growRoomRepository;

    public CycleServiceImpl(CycleRepository cycleRepository,GrowRoomRepository growRoomRepository) {
        this.cycleRepository = cycleRepository;
        this.growRoomRepository = growRoomRepository;
    }

    @Override
    public List<Cycle> getAll() {
        return cycleRepository.findAll();
    }

    @Override
    public Cycle getById(Long id) {
        return cycleRepository.findById(id).orElseThrow();
    }

    @Override
public Cycle save(Cycle cycle) {
    // Ensure grow room is valid and fetched
    Long growRoomId = cycle.getGrowRoom() != null ? cycle.getGrowRoom().getId() : null;
    if (growRoomId == null) {
        throw new IllegalArgumentException("Grow room must be selected.");
    }

    GrowRoom growRoom = growRoomRepository.findById(growRoomId)
            .orElseThrow(() -> new RuntimeException("Grow room not found"));

    cycle.setGrowRoom(growRoom); // Now contains name

    if (cycle.getName() == null || cycle.getName().isBlank()) {
        String baseName = growRoom.getName().replaceAll("\\s+", "") + "_" + cycle.getStartDate();
        String uniqueName = baseName;
        int counter = 1;
        while (cycleRepository.existsByName(uniqueName)) {
            uniqueName = baseName + "_" + counter++;
        }
        cycle.setName(uniqueName);
    }

    return cycleRepository.save(cycle);
}


    @Override
    public void delete(Long id) {
        cycleRepository.deleteById(id);
    }

    @Override
    public String generateCycleName(Long growRoomId, String growRoomName, LocalDate startDate) {
        return growRoomName.replaceAll("\\s+", "") + "_" + startDate;
    }
}
