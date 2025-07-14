package com.mariasorganics.farmtracker.service.impl;

import com.mariasorganics.farmtracker.entity.GrowRoom;
import com.mariasorganics.farmtracker.repository.GrowRoomRepository;
import com.mariasorganics.farmtracker.service.IGrowRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrowRoomServiceImpl implements IGrowRoomService {

    private final GrowRoomRepository repo;

    public GrowRoomServiceImpl(GrowRoomRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<GrowRoom> getAll() {
        return repo.findAll();
    }

    @Override
    public GrowRoom getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public GrowRoom save(GrowRoom room) {
        return repo.save(room);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
