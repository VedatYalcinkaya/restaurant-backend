package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.menucategory.requests.CreateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.requests.UpdateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.responses.MenuCategoryResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;

import java.util.List;

public interface MenuCategoryService {
    
    DataResult<List<MenuCategoryResponse>> getAll();
    
    DataResult<List<MenuCategoryResponse>> getAll(int page, int size);
    
    DataResult<List<MenuCategoryResponse>> getAllActive();
    
    DataResult<List<MenuCategoryResponse>> getAllActive(int page, int size);
    
    DataResult<MenuCategoryResponse> getById(Long id);
    
    DataResult<MenuCategoryResponse> add(CreateMenuCategoryRequest createMenuCategoryRequest);
    
    DataResult<MenuCategoryResponse> update(Long id, UpdateMenuCategoryRequest updateMenuCategoryRequest);
    
    Result delete(Long id);
    
    Result activate(Long id);
    
    Result deactivate(Long id);
} 