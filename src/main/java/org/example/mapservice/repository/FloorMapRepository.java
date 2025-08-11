package org.example.mapservice.repository;

import org.example.mapservice.entity.FloorMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapRepository extends JpaRepository<FloorMapEntity, Long> {
    List<FloorMapEntity> findByFloor_Id(Long floorId);
}
