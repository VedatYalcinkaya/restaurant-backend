package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.menucategory.requests.CreateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.requests.UpdateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.responses.MenuCategoryResponse;
import com.demirciyazilim.entities.MenuCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuCategoryMapper {

    public MenuCategory toEntity(CreateMenuCategoryRequest request) {
        MenuCategory category = new MenuCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.isActive());
        return category;
    }
    
    public MenuCategory toEntity(UpdateMenuCategoryRequest request) {
        MenuCategory category = new MenuCategory();
        category.setId(request.getId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.isActive());
        return category;
    }

    public MenuCategoryResponse toResponse(MenuCategory entity) {
        MenuCategoryResponse response = new MenuCategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setActive(entity.isActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public List<MenuCategoryResponse> toResponseList(List<MenuCategory> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(UpdateMenuCategoryRequest request, MenuCategory entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setActive(request.isActive());
    }
} 