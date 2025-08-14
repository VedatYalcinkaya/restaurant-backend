package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.Menu;
import com.demirciyazilim.entities.MenuCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    List<Menu> findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(MenuCategory category);
    
    List<Menu> findByCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(Long categoryId);
    
    Page<Menu> findByIsActiveTrue(Pageable pageable);
    
    Page<Menu> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
    
    List<Menu> findByIsActiveTrueAndIsAvailableTrueOrderByCategoryDisplayOrderAscDisplayOrderAsc();
    
    Page<Menu> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
    
    @Query("SELECT m FROM Menu m WHERE m.isActive = true AND m.price BETWEEN :minPrice AND :maxPrice ORDER BY m.category.displayOrder ASC, m.displayOrder ASC")
    List<Menu> findByPriceBetweenAndIsActiveTrue(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT m FROM Menu m WHERE m.isActive = true AND m.isAvailable = true AND m.category.id = :categoryId ORDER BY m.displayOrder ASC")
    List<Menu> findAvailableMenuItemsByCategory(@Param("categoryId") Long categoryId);
    
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    
    boolean existsByNameAndCategoryIdAndIdNot(String name, Long categoryId, Long id);
    
    Optional<Menu> findByIdAndIsActiveTrue(Long id);
} 