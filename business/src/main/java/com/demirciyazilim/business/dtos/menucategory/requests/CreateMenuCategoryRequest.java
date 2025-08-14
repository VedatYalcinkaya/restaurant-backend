package com.demirciyazilim.business.dtos.menucategory.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuCategoryRequest {
    
    @NotBlank(message = "Kategori adı boş olamaz")
    @Size(min = 2, max = 100, message = "Kategori adı 2-100 karakter arasında olmalıdır")
    private String name;
    
    @Size(max = 500, message = "Açıklama en fazla 500 karakter olmalıdır")
    private String description;
    
    private int displayOrder = 0;
    
    private boolean active = true;
} 