package com.demirciyazilim.business.rules;

import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.repositories.MenuCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MenuCategoryBusinessRules {
    
    private final MenuCategoryRepository menuCategoryRepository;
    
    public void checkIfMenuCategoryExists(Long id) {
        if (!menuCategoryRepository.existsById(id)) {
            throw new BusinessException("Menü kategorisi bulunamadı");
        }
    }
    
    public void checkIfCategoryNameExists(String name) {
        if (menuCategoryRepository.existsByName(name)) {
            throw new BusinessException("Bu kategori adı zaten kullanılıyor: " + name);
        }
    }
    
    public void checkIfCategoryNameExistsForUpdate(String name, Long id) {
        if (menuCategoryRepository.existsByNameAndIdNot(name, id)) {
            throw new BusinessException("Bu kategori adı zaten başka bir kategori tarafından kullanılıyor: " + name);
        }
    }
} 