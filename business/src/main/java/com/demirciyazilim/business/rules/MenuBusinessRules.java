package com.demirciyazilim.business.rules;

import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.repositories.MenuCategoryRepository;
import com.demirciyazilim.repositories.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MenuBusinessRules {
    
    private final MenuRepository menuRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    
    public void checkIfMenuExists(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new BusinessException("Menü öğesi bulunamadı");
        }
    }
    
    public void checkIfCategoryExists(Long categoryId) {
        if (!menuCategoryRepository.existsById(categoryId)) {
            throw new BusinessException("Belirtilen kategori bulunamadı");
        }
    }
    
    public void checkIfMenuNameExistsInCategory(String name, Long categoryId) {
        if (menuRepository.existsByNameAndCategoryId(name, categoryId)) {
            throw new BusinessException("Bu kategoride aynı isimde bir menü öğesi zaten var: " + name);
        }
    }
    
    public void checkIfMenuNameExistsInCategoryForUpdate(String name, Long categoryId, Long menuId) {
        if (menuRepository.existsByNameAndCategoryIdAndIdNot(name, categoryId, menuId)) {
            throw new BusinessException("Bu kategoride aynı isimde başka bir menü öğesi zaten var: " + name);
        }
    }
} 