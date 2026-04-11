package com.pointr.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    private String id;
    private String siteId;
    private String name;
    private String description;
    private Integer floorCount;
    private String createdAt;
}