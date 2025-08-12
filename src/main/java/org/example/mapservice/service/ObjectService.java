package org.example.mapservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mapservice.dto.ObjectDto;
import org.example.mapservice.dto.ObjectRequest;
import org.example.mapservice.entity.ObjectEntity;
import org.example.mapservice.repository.FloorRepository;
import org.example.mapservice.repository.ObjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final ObjectRepository repo;
    private final FloorRepository floorRepository;  // репозиторий этажей

    public ObjectDto create(ObjectRequest req) {
        if (req.getFloorId() == null) throw new IllegalArgumentException("floorId required");
        if (req.getX() == null || req.getY() == null) throw new IllegalArgumentException("x,y required");
        if (req.getType() == null || req.getType().isBlank()) throw new IllegalArgumentException("type required");

        boolean exists = floorRepository.existsById(req.getFloorId());
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor with id " + req.getFloorId() + " not found");
        }

        ObjectEntity e = ObjectEntity.builder()
                .floorId(req.getFloorId())
                .x(req.getX())
                .y(req.getY())
                .type(req.getType().toUpperCase())
                .label(req.getLabel())
                .createdAt(Instant.now())
                .build();

        ObjectEntity saved = repo.save(e);
        return toDto(saved);
    }

    public ObjectDto get(Long id) {
        ObjectEntity e = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found"));
        return toDto(e);
    }


    public List<ObjectDto> listByFloor(Long floorId) {
        return repo.findByFloorId(floorId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ObjectDto> listByType(String type) {
        return repo.findByTypeIgnoreCase(type).stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found"));
        repo.deleteById(id);
    }

    private ObjectDto toDto(ObjectEntity e) {
        return ObjectDto.builder()
                .id(e.getId())
                .floorId(e.getFloorId())
                .x(e.getX())
                .y(e.getY())
                .type(e.getType())
                .label(e.getLabel())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
