package org.example.mapservice.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectDto {
    private Long id;
    private Long floorId;
    private Double x;
    private Double y;
    private String type;
    private String label;
    private Instant createdAt;
}
