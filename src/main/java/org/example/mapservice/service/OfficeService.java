package org.example.mapservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mapservice.dto.FloorDto;
import org.example.mapservice.dto.OfficeDto;
import org.example.mapservice.entity.FloorEntity;
import org.example.mapservice.entity.OfficeEntity;
import org.example.mapservice.repository.FloorRepository;
import org.example.mapservice.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final FloorRepository floorRepository;

    // Получение всех офисов с этажами в виде DTO
    public List<OfficeDto> getAllOffices() {
        List<OfficeEntity> entities = officeRepository.findAll();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Создание офиса с этажами из DTO, возвращаем DTO
    public OfficeDto createOffice(OfficeDto request) {
        OfficeEntity office = new OfficeEntity();
        office.setName(request.getName());
        office.setAddress(request.getAddress());

        List<FloorEntity> floors = new ArrayList<>();
        if (request.getFloors() != null) {
            for (FloorDto f : request.getFloors()) {
                FloorEntity floor = new FloorEntity();
                floor.setFloorNumber(f.getFloorNumber());
                floor.setOffice(office);
                floors.add(floor);
            }
        }
        office.setFloors(floors);

        OfficeEntity saved = officeRepository.save(office);
        return toDto(saved);
    }

    // Создание отдельного этажа для существующего офиса
    public FloorEntity createFloor(Long officeId, FloorEntity floor) {
        OfficeEntity office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("Office not found"));
        floor.setOffice(office);
        return floorRepository.save(floor);
    }

    // Преобразование OfficeEntity в OfficeDto с вложенными FloorDto
    private OfficeDto toDto(OfficeEntity office) {
        List<FloorDto> floorsDto = office.getFloors() == null ? List.of() :
                office.getFloors().stream()
                        .map(floor -> new FloorDto(floor.getId(), floor.getFloorNumber()))
                        .collect(Collectors.toList());

        return OfficeDto.builder()
                .id(office.getId())
                .name(office.getName())
                .address(office.getAddress())
                .floors(floorsDto)
                .build();
    }
}
