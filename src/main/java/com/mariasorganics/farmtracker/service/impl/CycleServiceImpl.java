package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.Cycle;
import com.mariasorganics.farmtracker.entity.GrowRoom;
import com.mariasorganics.farmtracker.repository.CycleRepository;
import com.mariasorganics.farmtracker.repository.GrowRoomRepository;
import com.mariasorganics.farmtracker.service.ICycleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CycleServiceImpl implements ICycleService {

    private final CycleRepository cycleRepository;

    private final GrowRoomRepository growRoomRepository;

    public CycleServiceImpl(CycleRepository cycleRepository, GrowRoomRepository growRoomRepository) {
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

        cycle.setGrowRoom(growRoom); // populate grow room with full details

        // 🔒 Enforce only one ACTIVE cycle per grow room
        if (cycle.getStatus() == Cycle.Status.ACTIVE) {
            boolean alreadyHasActive = cycleRepository.existsByGrowRoom_IdAndStatus(growRoomId, Cycle.Status.ACTIVE);

            boolean isUpdatingSameCycle = (cycle.getId() != null)
                    && cycleRepository.findById(cycle.getId())
                            .map(existing -> existing.getStatus() == Cycle.Status.ACTIVE)
                            .orElse(false);

            if (alreadyHasActive && !isUpdatingSameCycle) {
                throw new IllegalArgumentException("Only one ACTIVE cycle is allowed per grow room.");
            }
        }

        // Auto-generate name if not provided
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

    @Override
    public Optional<Cycle> findActiveCycleByGrowRoomId(Long growRoomId) {
        return cycleRepository.findByGrowRoom_IdAndStatus(growRoomId, Cycle.Status.ACTIVE);
    }

}
