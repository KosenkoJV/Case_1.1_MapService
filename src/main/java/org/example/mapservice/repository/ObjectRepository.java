package org.example.mapservice.repository;

import org.example.mapservice.entity.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, Long> {
    List<ObjectEntity> findByFloorId(Long floorId);
    List<ObjectEntity> findByTypeIgnoreCase(String type);
}
