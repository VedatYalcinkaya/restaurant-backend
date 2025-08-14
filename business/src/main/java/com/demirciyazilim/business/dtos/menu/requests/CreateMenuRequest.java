package com.demirciyazilim.business.dtos.menu.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuRequest {
    
    @NotBlank(message = "Yemek adı boş olamaz")
    @Size(min = 2, max = 200, message = "Yemek adı 2-200 karakter arasında olmalıdır")
    private String name;
    
    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olmalıdır")
    private String description;
    
    @NotNull(message = "Fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fiyat 0'dan büyük olmalıdır")
    private BigDecimal price;
    
    @Size(max = 500, message = "İçerikler en fazla 500 karakter olmalıdır")
    private String ingredients;
    
    private String imageUrl;
    
    @NotNull(message = "Kategori ID boş olamaz")
    private Long categoryId;
    
    private boolean available = true;
    
    private boolean active = true;
    
    private int displayOrder = 0;
    
    @Size(max = 100, message = "Alerjenler en fazla 100 karakter olmalıdır")
    private String allergens;
    
    private Integer calories;
    
    private Integer preparationTimeMinutes;
} 