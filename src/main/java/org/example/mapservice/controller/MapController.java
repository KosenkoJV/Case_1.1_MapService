package org.example.mapservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.mapservice.dto.MapDto;
import org.example.mapservice.dto.OfficeDto;
import org.example.mapservice.dto.ObjectDto;
import org.example.mapservice.dto.ObjectRequest;
import org.example.mapservice.entity.FloorEntity;
import org.example.mapservice.service.MapService;
import org.example.mapservice.service.OfficeService;
import org.example.mapservice.service.ObjectService;
import org.springframework.core.io.Resource;
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

    private final OfficeService officeService;
    private final MapService mapService;
    private final ObjectService objectService;

    // ===== OFFICE =====
    @GetMapping("/offices")
    public List<OfficeDto> getAllOffices() {
        return officeService.getAllOffices();
    }

    @PostMapping("/offices")
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto request) {
        return ResponseEntity.ok(officeService.createOffice(request));
    }

    // ===== FLOOR =====
    @PostMapping("/offices/{officeId}/floors")
    public FloorEntity createFloor(@PathVariable Long officeId, @RequestBody FloorEntity floor) {
        return officeService.createFloor(officeId, floor);
    }

    // ===== FLOOR MAP =====
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

    // ===== OBJECTS =====
    @PostMapping("/objects")
    public ResponseEntity<ObjectDto> createObject(@RequestBody ObjectRequest req) {
        return ResponseEntity.ok(objectService.create(req));
    }

    @GetMapping("/objects/{id}")
    public ResponseEntity<ObjectDto> getObject(@PathVariable Long id) {
        return ResponseEntity.ok(objectService.get(id));
    }

    @GetMapping("/objects/floor/{floorId}")
    public ResponseEntity<List<ObjectDto>> listObjectsByFloor(@PathVariable Long floorId) {
        return ResponseEntity.ok(objectService.listByFloor(floorId));
    }

    @GetMapping("/objects/type/{type}")
    public ResponseEntity<List<ObjectDto>> listObjectsByType(@PathVariable String type) {
        return ResponseEntity.ok(objectService.listByType(type));
    }

    @DeleteMapping("/objects/{id}")
    public ResponseEntity<Void> deleteObject(@PathVariable Long id) {
        objectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
