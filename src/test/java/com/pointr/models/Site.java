package com.pointr.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site {
    private String id;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private String createdAt;
}