package org.example.mapservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapDto {
    private Long id;
    private Long floorId;
    private String name;        // имя файла / название карты
    private BigDecimal scale;
    private LocalDate uploadDate;
    private String downloadUrl; // ссылка на скачивание файла
}

