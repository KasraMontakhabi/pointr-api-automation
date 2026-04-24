package com.pointr.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelImportResponse {
    private String buildingId;
    private Integer imported;
    private List<Level> levels;
}
