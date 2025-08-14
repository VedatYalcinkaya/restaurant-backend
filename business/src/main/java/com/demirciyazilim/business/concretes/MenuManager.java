package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.MenuService;
import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.business.dtos.menu.requests.CreateMenuRequest;
import com.demirciyazilim.business.dtos.menu.requests.UpdateMenuRequest;
import com.demirciyazilim.business.dtos.menu.responses.MenuResponse;
import com.demirciyazilim.business.mappers.MenuMapper;
import com.demirciyazilim.business.rules.MenuBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.Menu;
import com.demirciyazilim.entities.MenuCategory;
import com.demirciyazilim.repositories.MenuCategoryRepository;
import com.demirciyazilim.repositories.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuManager implements MenuService {
    
    private final MenuRepository menuRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuBusinessRules menuBusinessRules;
    private final MenuMapper menuMapper;
    
    @Override
    public DataResult<List<MenuResponse>> getAll() {
        List<Menu> menus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "category.displayOrder", "displayOrder"));
        List<MenuResponse> menuResponses = menuMapper.toResponseList(menus);
        return new SuccessDataResult<>(menuResponses, "Menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category.displayOrder", "displayOrder"));
        Page<Menu> result = menuRepository.findAll(pageable);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(menuResponses, "Menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getAllActive(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category.displayOrder", "displayOrder"));
        Page<Menu> result = menuRepository.findByIsActiveTrue(pageable);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(menuResponses, "Aktif menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getAllAvailable() {
        List<Menu> menus = menuRepository.findByIsActiveTrueAndIsAvailableTrueOrderByCategoryDisplayOrderAscDisplayOrderAsc();
        List<MenuResponse> menuResponses = menuMapper.toResponseList(menus);
        return new SuccessDataResult<>(menuResponses, "Mevcut menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getByCategory(Long categoryId) {
        menuBusinessRules.checkIfCategoryExists(categoryId);
        List<Menu> menus = menuRepository.findByCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(categoryId);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(menus);
        return new SuccessDataResult<>(menuResponses, "Kategoriye ait menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getByCategoryActive(Long categoryId, int page, int size) {
        menuBusinessRules.checkIfCategoryExists(categoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "displayOrder"));
        Page<Menu> result = menuRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(menuResponses, "Kategoriye ait aktif menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getByCategoryAvailable(Long categoryId) {
        menuBusinessRules.checkIfCategoryExists(categoryId);
        List<Menu> menus = menuRepository.findAvailableMenuItemsByCategory(categoryId);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(menus);
        return new SuccessDataResult<>(menuResponses, "Kategoriye ait mevcut menü öğeleri başarıyla listelendi");
    }
    
    @Override
    public DataResult<MenuResponse> getById(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            return new ErrorDataResult<>("Menü öğesi bulunamadı");
        }
        MenuResponse menuResponse = menuMapper.toResponse(menu.get());
        return new SuccessDataResult<>(menuResponse, "Menü öğesi başarıyla getirildi");
    }
    
    @Override
    public DataResult<MenuResponse> add(CreateMenuRequest createMenuRequest) {
        menuBusinessRules.checkIfCategoryExists(createMenuRequest.getCategoryId());
        menuBusinessRules.checkIfMenuNameExistsInCategory(createMenuRequest.getName(), createMenuRequest.getCategoryId());
        
        Menu menu = menuMapper.toEntity(createMenuRequest);
        
        // Category nesnesini repository'den al
        MenuCategory category = menuCategoryRepository.findById(createMenuRequest.getCategoryId()).get();
        menu.setCategory(category);
        
        menu.setCreatedAt(LocalDateTime.now());
        Menu savedMenu = menuRepository.save(menu);
        MenuResponse menuResponse = menuMapper.toResponse(savedMenu);
        return new SuccessDataResult<>(menuResponse, "Menü öğesi başarıyla eklendi");
    }
    
    @Override
    public DataResult<MenuResponse> update(Long id, UpdateMenuRequest updateMenuRequest) {
        menuBusinessRules.checkIfMenuExists(id);
        menuBusinessRules.checkIfCategoryExists(updateMenuRequest.getCategoryId());
        menuBusinessRules.checkIfMenuNameExistsInCategoryForUpdate(updateMenuRequest.getName(), updateMenuRequest.getCategoryId(), id);
        
        Menu existingMenu = menuRepository.findById(id).get();
        menuMapper.updateEntityFromRequest(updateMenuRequest, existingMenu);
        
        // Category nesnesini repository'den al
        MenuCategory category = menuCategoryRepository.findById(updateMenuRequest.getCategoryId()).get();
        existingMenu.setCategory(category);
        
        existingMenu.setUpdatedAt(LocalDateTime.now());
        Menu updatedMenu = menuRepository.save(existingMenu);
        MenuResponse menuResponse = menuMapper.toResponse(updatedMenu);
        return new SuccessDataResult<>(menuResponse, "Menü öğesi başarıyla güncellendi");
    }
    
    @Override
    public Result delete(Long id) {
        menuBusinessRules.checkIfMenuExists(id);
        menuRepository.deleteById(id);
        return new SuccessResult("Menü öğesi başarıyla silindi");
    }
    
    @Override
    public Result activate(Long id) {
        menuBusinessRules.checkIfMenuExists(id);
        Menu menu = menuRepository.findById(id).get();
        menu.setActive(true);
        menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);
        return new SuccessResult("Menü öğesi başarıyla aktifleştirildi");
    }
    
    @Override
    public Result deactivate(Long id) {
        menuBusinessRules.checkIfMenuExists(id);
        Menu menu = menuRepository.findById(id).get();
        menu.setActive(false);
        menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);
        return new SuccessResult("Menü öğesi başarıyla deaktifleştirildi");
    }
    
    @Override
    public Result makeAvailable(Long id) {
        menuBusinessRules.checkIfMenuExists(id);
        Menu menu = menuRepository.findById(id).get();
        menu.setAvailable(true);
        menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);
        return new SuccessResult("Menü öğesi mevcut duruma getirildi");
    }
    
    @Override
    public Result makeUnavailable(Long id) {
        menuBusinessRules.checkIfMenuExists(id);
        Menu menu = menuRepository.findById(id).get();
        menu.setAvailable(false);
        menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);
        return new SuccessResult("Menü öğesi mevcut değil duruma getirildi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category.displayOrder", "displayOrder"));
        Page<Menu> result = menuRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(menuResponses, "Arama sonuçları başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuResponse>> getByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Menu> menus = menuRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
        List<MenuResponse> menuResponses = menuMapper.toResponseList(menus);
        return new SuccessDataResult<>(menuResponses, "Fiyat aralığındaki menü öğeleri başarıyla listelendi");
    }
} 