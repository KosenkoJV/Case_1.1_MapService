package org.example.mapservice.repository;

import org.example.mapservice.entity.OfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<OfficeEntity, Long> {
}
