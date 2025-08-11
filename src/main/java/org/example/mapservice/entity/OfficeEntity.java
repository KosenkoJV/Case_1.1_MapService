package org.example.mapservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "office")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "office_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;
}
