package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.MenuCategoryService;
import com.demirciyazilim.business.dtos.menucategory.requests.CreateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.requests.UpdateMenuCategoryRequest;
import com.demirciyazilim.business.dtos.menucategory.responses.MenuCategoryResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-categories")
@AllArgsConstructor
@Tag(name = "Menu Categories", description = "Menü Kategorileri API")
@CrossOrigin
public class MenuCategoriesController {

    private final MenuCategoryService menuCategoryService;

    @GetMapping
    @Operation(summary = "Tüm menü kategorilerini getir", description = "Tüm menü kategorilerinin listesini döndürür")
    public ResponseEntity<DataResult<List<MenuCategoryResponse>>> getAll() {
        return ResponseEntity.ok(menuCategoryService.getAll());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Tüm menü kategorilerini sayfalı getir", description = "Tüm menü kategorilerini sayfalama ile döndürür")
    public ResponseEntity<DataResult<List<MenuCategoryResponse>>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuCategoryService.getAll(page, size));
    }

    @GetMapping("/active")
    @Operation(summary = "Aktif menü kategorilerini getir", description = "Aktif menü kategorilerini döndürür")
    public ResponseEntity<DataResult<List<MenuCategoryResponse>>> getAllActive() {
        return ResponseEntity.ok(menuCategoryService.getAllActive());
    }

    @GetMapping("/active/paginated")
    @Operation(summary = "Aktif menü kategorilerini sayfalı getir", description = "Aktif menü kategorilerini sayfalama ile döndürür")
    public ResponseEntity<DataResult<List<MenuCategoryResponse>>> getAllActivePaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuCategoryService.getAllActive(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile menü kategorisi getir", description = "Belirtilen ID'ye sahip menü kategorisini getirir")
    public ResponseEntity<DataResult<MenuCategoryResponse>> getById(@PathVariable Long id) {
        DataResult<MenuCategoryResponse> result = menuCategoryService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    @Operation(
        summary = "Menü kategorisi ekle", 
        description = "Yeni bir menü kategorisi ekler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuCategoryResponse>> add(@Valid @RequestBody CreateMenuCategoryRequest createMenuCategoryRequest) {
        DataResult<MenuCategoryResponse> result = menuCategoryService.add(createMenuCategoryRequest);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Menü kategorisi güncelle", 
        description = "Var olan bir menü kategorisini günceller",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuCategoryResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateMenuCategoryRequest updateMenuCategoryRequest) {
        DataResult<MenuCategoryResponse> result = menuCategoryService.update(id, updateMenuCategoryRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menü kategorisi sil", 
        description = "Belirtilen ID'ye sahip menü kategorisini siler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result result = menuCategoryService.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/activate/{id}")
    @Operation(
        summary = "Menü kategorisi aktifleştir", 
        description = "Belirtilen ID'ye sahip menü kategorisini aktifleştirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> activate(@PathVariable Long id) {
        Result result = menuCategoryService.activate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(
        summary = "Menü kategorisi deaktifleştir", 
        description = "Belirtilen ID'ye sahip menü kategorisini deaktifleştirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> deactivate(@PathVariable Long id) {
        Result result = menuCategoryService.deactivate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
} 