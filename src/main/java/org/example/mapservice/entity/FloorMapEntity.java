package org.example.mapservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "floor_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FloorMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private FloorEntity floor;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;

    @Column(precision = 5, scale = 2)
    private BigDecimal scale;

    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "size")
    private Long size;
}