package org.example.mapservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mapservice.dto.MapDto;
import org.example.mapservice.entity.FloorEntity;
import org.example.mapservice.entity.FloorMapEntity;
import org.example.mapservice.repository.FloorMapRepository;
import org.example.mapservice.repository.FloorRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final FloorMapRepository floorMapRepository;
    private final FloorRepository floorRepository;

    private final String storageDir = "uploads";

    // Загрузка карты
    public MapDto uploadMap(Long floorId, MultipartFile file, BigDecimal scale, String name) throws IOException {
        FloorEntity floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        Files.createDirectories(Path.of(storageDir));
        String originalName = (name != null && !name.isEmpty()) ? name : file.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalName;
        Path filePath = Path.of(storageDir, filename);
        file.transferTo(filePath); // Сохраняем файл на диск

        // Получаем mime-type и размер
        String mimeType = file.getContentType();
        long size = Files.size(filePath);

        FloorMapEntity entity = FloorMapEntity.builder()
                .floor(floor)
                .name(originalName)
                .filePath(filePath.toString())
                .scale(scale)
                .uploadDate(LocalDate.now())
                .mimeType(mimeType)
                .size(size)
                .build();
        floorMapRepository.save(entity);
        return mapToDto(entity);
    }

    // Получить список карт по этажу
    public List<MapDto> listByFloor(Long floorId) {
        return floorMapRepository.findByFloor_Id(floorId).stream()
                .map(this::mapToDto)
                .toList();
    }

    // Скачать файл карты
    public ResponseEntity<Resource> downloadMapResource(Long mapId) throws IOException {
        FloorMapEntity map = floorMapRepository.findById(mapId)
                .orElseThrow(() -> new RuntimeException("Map not found"));

        Path file = Path.of(map.getFilePath());
        if (!Files.exists(file)) {
            throw new RuntimeException("File not found on disk");
        }

        Resource resource = new UrlResource(file.toUri());
        String contentType = map.getMimeType();
        if (contentType == null) {
            contentType = Files.probeContentType(file);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + map.getName() + "\"")
                .body(resource);
    }

    // Удалить карту
    public void deleteMap(Long mapId) throws IOException {
        FloorMapEntity map = floorMapRepository.findById(mapId)
                .orElseThrow(() -> new RuntimeException("Map not found"));

        Files.deleteIfExists(Path.of(map.getFilePath()));
        floorMapRepository.delete(map);
    }

    // Маппинг Entity в DTO
    private MapDto mapToDto(FloorMapEntity e) {
        return MapDto.builder()
                .id(e.getId())
                .floorId(e.getFloor().getId())
                .name(e.getName())
                .scale(e.getScale())
                .uploadDate(e.getUploadDate())
                .downloadUrl("/api/maps/" + e.getId() + "/download")
                .build();
    }
}
