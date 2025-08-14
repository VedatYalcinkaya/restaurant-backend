package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.MenuCategoryService;
import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.business.dtos.menucategory.requests.CreateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.requests.UpdateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.responses.MenuCategoryResponse;
import com.demirciyazilim.business.mappers.MenuCategoryMapper;
import com.demirciyazilim.business.rules.MenuCategoryBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.MenuCategory;
import com.demirciyazilim.repositories.MenuCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuCategoryManager implements MenuCategoryService {
    
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuCategoryBusinessRules menuCategoryBusinessRules;
    private final MenuCategoryMapper menuCategoryMapper;
    
    @Override
    public DataResult<List<MenuCategoryResponse>> getAll() {
        List<MenuCategory> categories = menuCategoryRepository.findAllByOrderByDisplayOrderAsc();
        List<MenuCategoryResponse> categoryResponses = menuCategoryMapper.toResponseList(categories);
        return new SuccessDataResult<>(categoryResponses, "Menü kategorileri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuCategoryResponse>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "displayOrder"));
        Page<MenuCategory> result = menuCategoryRepository.findAll(pageable);
        List<MenuCategoryResponse> categoryResponses = menuCategoryMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(categoryResponses, "Menü kategorileri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuCategoryResponse>> getAllActive() {
        List<MenuCategory> categories = menuCategoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        List<MenuCategoryResponse> categoryResponses = menuCategoryMapper.toResponseList(categories);
        return new SuccessDataResult<>(categoryResponses, "Aktif menü kategorileri başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<MenuCategoryResponse>> getAllActive(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "displayOrder"));
        Page<MenuCategory> result = menuCategoryRepository.findByIsActiveTrue(pageable);
        List<MenuCategoryResponse> categoryResponses = menuCategoryMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(categoryResponses, "Aktif menü kategorileri başarıyla listelendi");
    }
    
    @Override
    public DataResult<MenuCategoryResponse> getById(Long id) {
        Optional<MenuCategory> category = menuCategoryRepository.findById(id);
        if (category.isEmpty()) {
            return new ErrorDataResult<>("Menü kategorisi bulunamadı");
        }
        MenuCategoryResponse categoryResponse = menuCategoryMapper.toResponse(category.get());
        return new SuccessDataResult<>(categoryResponse, "Menü kategorisi başarıyla getirildi");
    }
    
    @Override
    public DataResult<MenuCategoryResponse> add(CreateMenuCategoryRequest createMenuCategoryRequest) {
        menuCategoryBusinessRules.checkIfCategoryNameExists(createMenuCategoryRequest.getName());
        
        MenuCategory category = menuCategoryMapper.toEntity(createMenuCategoryRequest);
        category.setCreatedAt(LocalDateTime.now());
        MenuCategory savedCategory = menuCategoryRepository.save(category);
        MenuCategoryResponse categoryResponse = menuCategoryMapper.toResponse(savedCategory);
        return new SuccessDataResult<>(categoryResponse, "Menü kategorisi başarıyla eklendi");
    }
    
    @Override
    public DataResult<MenuCategoryResponse> update(Long id, UpdateMenuCategoryRequest updateMenuCategoryRequest) {
        menuCategoryBusinessRules.checkIfMenuCategoryExists(id);
        menuCategoryBusinessRules.checkIfCategoryNameExistsForUpdate(updateMenuCategoryRequest.getName(), id);
        
        MenuCategory existingCategory = menuCategoryRepository.findById(id).get();
        menuCategoryMapper.updateEntityFromRequest(updateMenuCategoryRequest, existingCategory);
        existingCategory.setUpdatedAt(LocalDateTime.now());
        MenuCategory updatedCategory = menuCategoryRepository.save(existingCategory);
        MenuCategoryResponse categoryResponse = menuCategoryMapper.toResponse(updatedCategory);
        return new SuccessDataResult<>(categoryResponse, "Menü kategorisi başarıyla güncellendi");
    }
    
    @Override
    public Result delete(Long id) {
        menuCategoryBusinessRules.checkIfMenuCategoryExists(id);
        menuCategoryRepository.deleteById(id);
        return new SuccessResult("Menü kategorisi başarıyla silindi");
    }
    
    @Override
    public Result activate(Long id) {
        menuCategoryBusinessRules.checkIfMenuCategoryExists(id);
        MenuCategory category = menuCategoryRepository.findById(id).get();
        category.setActive(true);
        category.setUpdatedAt(LocalDateTime.now());
        menuCategoryRepository.save(category);
        return new SuccessResult("Menü kategorisi başarıyla aktifleştirildi");
    }
    
    @Override
    public Result deactivate(Long id) {
        menuCategoryBusinessRules.checkIfMenuCategoryExists(id);
        MenuCategory category = menuCategoryRepository.findById(id).get();
        category.setActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        menuCategoryRepository.save(category);
        return new SuccessResult("Menü kategorisi başarıyla deaktifleştirildi");
    }
} 