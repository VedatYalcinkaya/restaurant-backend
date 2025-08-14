package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.menu.requests.CreateMenuRequest;
import com.demirciyazilim.business.dtos.menu.requests.UpdateMenuRequest;
import com.demirciyazilim.business.dtos.menu.responses.MenuResponse;
import com.demirciyazilim.entities.Menu;
import com.demirciyazilim.entities.MenuCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MenuMapper {
    
    private final MenuCategoryMapper menuCategoryMapper;

    public Menu toEntity(CreateMenuRequest request) {
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setPrice(request.getPrice());
        menu.setIngredients(request.getIngredients());
        menu.setImageUrl(request.getImageUrl());
        menu.setAvailable(request.isAvailable());
        menu.setActive(request.isActive());
        menu.setDisplayOrder(request.getDisplayOrder());
        menu.setAllergens(request.getAllergens());
        menu.setCalories(request.getCalories());
        menu.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        
        // Category sadece ID ile set edilecek, service katmanında tam nesne atanacak
        MenuCategory category = new MenuCategory();
        category.setId(request.getCategoryId());
        menu.setCategory(category);
        
        return menu;
    }
    
    public Menu toEntity(UpdateMenuRequest request) {
        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setPrice(request.getPrice());
        menu.setIngredients(request.getIngredients());
        menu.setImageUrl(request.getImageUrl());
        menu.setAvailable(request.isAvailable());
        menu.setActive(request.isActive());
        menu.setDisplayOrder(request.getDisplayOrder());
        menu.setAllergens(request.getAllergens());
        menu.setCalories(request.getCalories());
        menu.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        
        // Category sadece ID ile set edilecek, service katmanında tam nesne atanacak
        MenuCategory category = new MenuCategory();
        category.setId(request.getCategoryId());
        menu.setCategory(category);
        
        return menu;
    }

    public MenuResponse toResponse(Menu entity) {
        MenuResponse response = new MenuResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setIngredients(entity.getIngredients());
        response.setImageUrl(entity.getImageUrl());
        response.setAvailable(entity.isAvailable());
        response.setActive(entity.isActive());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setAllergens(entity.getAllergens());
        response.setCalories(entity.getCalories());
        response.setPreparationTimeMinutes(entity.getPreparationTimeMinutes());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        // Category response
        if (entity.getCategory() != null) {
            response.setCategory(menuCategoryMapper.toResponse(entity.getCategory()));
        }
        
        return response;
    }

    public List<MenuResponse> toResponseList(List<Menu> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(UpdateMenuRequest request, Menu entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setIngredients(request.getIngredients());
        entity.setImageUrl(request.getImageUrl());
        entity.setAvailable(request.isAvailable());
        entity.setActive(request.isActive());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setAllergens(request.getAllergens());
        entity.setCalories(request.getCalories());
        entity.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        
        // Category güncellenecek, service katmanında tam nesne atanacak
        MenuCategory category = new MenuCategory();
        category.setId(request.getCategoryId());
        entity.setCategory(category);
    }
} 