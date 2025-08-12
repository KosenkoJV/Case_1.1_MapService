package org.example.mapservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectRequest {
    private Long floorId;
    private Double x;
    private Double y;
    private String type;
    private String label;
}
