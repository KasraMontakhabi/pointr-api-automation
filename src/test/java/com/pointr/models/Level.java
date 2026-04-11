package com.pointr.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Level {
    private String id;
    private String buildingId;
    private String name;
    private Integer levelIndex;
    private String description;
    private String createdAt;
}