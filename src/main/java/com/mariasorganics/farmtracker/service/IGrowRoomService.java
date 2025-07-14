package com.mariasorganics.farmtracker.service;

import com.mariasorganics.farmtracker.entity.GrowRoom;
import java.util.List;

public interface IGrowRoomService {
    List<GrowRoom> getAll();
    GrowRoom getById(Long id);
    GrowRoom save(GrowRoom room);
    void delete(Long id);
}
