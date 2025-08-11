package org.example.mapservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "floor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FloorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private OfficeEntity office;

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @Column(columnDefinition = "TEXT")
    private String description;
}
