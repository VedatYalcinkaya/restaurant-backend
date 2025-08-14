package com.demirciyazilim.business.dtos.menu.responses;

import com.demirciyazilim.business.dtos.menucategory.responses.MenuCategoryResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String ingredients;
    private String imageUrl;
    private boolean available;
    private boolean active;
    private int displayOrder;
    private String allergens;
    private Integer calories;
    private Integer preparationTimeMinutes;
    
    private MenuCategoryResponse category;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updatedAt;
} 