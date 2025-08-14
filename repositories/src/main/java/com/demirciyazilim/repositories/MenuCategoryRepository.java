package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.MenuCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    
    List<MenuCategory> findByIsActiveTrueOrderByDisplayOrderAsc();
    
    Page<MenuCategory> findByIsActiveTrue(Pageable pageable);
    
    Optional<MenuCategory> findByNameAndIsActiveTrue(String name);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
    
    List<MenuCategory> findAllByOrderByDisplayOrderAsc();
} 