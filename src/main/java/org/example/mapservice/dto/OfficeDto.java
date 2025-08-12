package org.example.mapservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficeDto {
    private Long id;
    private String name;
    private String address;
    private List<FloorDto> floors;
}
