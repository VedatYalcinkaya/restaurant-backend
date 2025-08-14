package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.menu.requests.CreateMenuRequest;
import com.demirciyazilim.business.dtos.menu.requests.UpdateMenuRequest;
import com.demirciyazilim.business.dtos.menu.responses.MenuResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;

import java.math.BigDecimal;
import java.util.List;

public interface MenuService {
    
    DataResult<List<MenuResponse>> getAll();
    
    DataResult<List<MenuResponse>> getAll(int page, int size);
    
    DataResult<List<MenuResponse>> getAllActive(int page, int size);
    
    DataResult<List<MenuResponse>> getAllAvailable();
    
    DataResult<List<MenuResponse>> getByCategory(Long categoryId);
    
    DataResult<List<MenuResponse>> getByCategoryActive(Long categoryId, int page, int size);
    
    DataResult<List<MenuResponse>> getByCategoryAvailable(Long categoryId);
    
    DataResult<MenuResponse> getById(Long id);
    
    DataResult<MenuResponse> add(CreateMenuRequest createMenuRequest);
    
    DataResult<MenuResponse> update(Long id, UpdateMenuRequest updateMenuRequest);
    
    Result delete(Long id);
    
    Result activate(Long id);
    
    Result deactivate(Long id);
    
    Result makeAvailable(Long id);
    
    Result makeUnavailable(Long id);
    
    DataResult<List<MenuResponse>> searchByName(String name, int page, int size);
    
    DataResult<List<MenuResponse>> getByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
} 