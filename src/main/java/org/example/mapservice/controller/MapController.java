package org.example.mapservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.mapservice.dto.MapDto;
import org.example.mapservice.entity.FloorEntity;
import org.example.mapservice.entity.FloorMapEntity;
import org.example.mapservice.entity.OfficeEntity;
import org.example.mapservice.repository.FloorMapRepository;
import org.example.mapservice.repository.FloorRepository;
import org.example.mapservice.repository.OfficeRepository;
import org.example.mapservice.service.MapService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MapController {

    private final OfficeRepository officeRepository;
    private final FloorRepository floorRepository;
    private final MapService mapService;

    // --- OFFICE (PROJECT) ---

    @GetMapping("/office")
    public List<OfficeEntity> getAllProjects() {
        return officeRepository.findAll();
    }

    @PostMapping("/office")
    public OfficeEntity createProject(@RequestBody OfficeEntity project) {
        return officeRepository.save(project);
    }

    // --- FLOOR ---

    @PostMapping("/office/{officeId}/floors")
    public FloorEntity createFloor(@PathVariable Long officeId, @RequestBody FloorEntity floor) {
        OfficeEntity office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("office not found"));
        floor.setOffice(office);
        return floorRepository.save(floor);
    }

    // --- FLOOR MAP ---

    @GetMapping("/floors/{floorId}/maps")
    public List<MapDto> getMapsByFloor(@PathVariable Long floorId) {
        return mapService.listByFloor(floorId);
    }

    @PostMapping("/floors/{floorId}/maps/upload")
    public MapDto uploadMap(@PathVariable Long floorId,
                            @RequestParam String name,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam(required = false) BigDecimal scale) throws Exception {
        return mapService.uploadMap(floorId, file, scale, name);
    }

    @GetMapping("/maps/{mapId}/download")
    public ResponseEntity<Resource> downloadMap(@PathVariable Long mapId) throws IOException {
        return mapService.downloadMapResource(mapId);
    }

    @DeleteMapping("/maps/{mapId}")
    public ResponseEntity<?> deleteMap(@PathVariable Long mapId) throws IOException {
        mapService.deleteMap(mapId);
        return ResponseEntity.ok().build();
    }
}
