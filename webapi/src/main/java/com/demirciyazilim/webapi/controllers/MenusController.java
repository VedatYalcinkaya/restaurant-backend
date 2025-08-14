package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.MenuService;
import com.demirciyazilim.business.dtos.menu.requests.CreateMenuRequest;
import com.demirciyazilim.business.dtos.menu.requests.UpdateMenuRequest;
import com.demirciyazilim.business.dtos.menu.responses.MenuResponse;
import com.demirciyazilim.core.utilities.file.CloudinaryService;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.ErrorDataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@AllArgsConstructor
@Tag(name = "Menus", description = "Menü API")
@CrossOrigin
public class MenusController {

    private final MenuService menuService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Menü resmi yükle", 
        description = "Cloudinary'ye menü resmi yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        ),
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<String>> uploadImage(
            @Parameter(description = "Yüklenecek dosya", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        DataResult<String> result = cloudinaryService.uploadFile(file, "menus/images");
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @PostMapping(value = "/create-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Menü oluştur ve resim yükle", 
        description = "Yeni bir menü oluşturur ve resmini Cloudinary'ye yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        ),
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuResponse>> createWithImage(
            @Parameter(description = "Menü bilgileri (JSON formatında)", required = true)
            @RequestParam("menuData") String menuDataJson,
            @Parameter(description = "Menü resmi", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("image") MultipartFile image) {
        
        try {
            // Resmi Cloudinary'ye yükle
            DataResult<String> imageResult = cloudinaryService.uploadFile(image, "menus/images");
            
            if (!imageResult.isSuccess()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorDataResult<>(imageResult.getMessage()));
            }
            
            // JSON verisini CreateMenuRequest nesnesine dönüştür
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            CreateMenuRequest createMenuRequest = objectMapper.readValue(menuDataJson, CreateMenuRequest.class);
            
            // Resim URL'sini ayarla
            createMenuRequest.setImageUrl(imageResult.getData());
            
            // Menüyü veritabanına ekle
            DataResult<MenuResponse> result = menuService.add(createMenuRequest);
            
            if (result.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDataResult<>("Menü oluşturulurken bir hata oluştu: " + e.getMessage()));
        }
    }
    
    @PostMapping(value = "/{id}/update-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Menü güncelle ve resim yükle", 
        description = "Var olan bir menüyü günceller ve yeni resmini Cloudinary'ye yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        ),
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuResponse>> updateWithImage(
            @PathVariable Long id,
            @Parameter(description = "Menü bilgileri (JSON formatında)", required = true)
            @RequestParam("menuData") String menuDataJson,
            @Parameter(description = "Menü resmi", required = false, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            // JSON verisini UpdateMenuRequest nesnesine dönüştür
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            UpdateMenuRequest updateMenuRequest = objectMapper.readValue(menuDataJson, UpdateMenuRequest.class);
            
            // ID path variable'dan al ve ilgili yere ekle
            updateMenuRequest.setId(id);
            
            // Eğer yeni bir resim yüklenmişse Cloudinary'ye yükle
            if (image != null && !image.isEmpty()) {
                DataResult<String> imageResult = cloudinaryService.uploadFile(image, "menus/images");
                
                if (!imageResult.isSuccess()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorDataResult<>(imageResult.getMessage()));
                }
                
                updateMenuRequest.setImageUrl(imageResult.getData());
            }
            
            // Menüyü güncelle
            DataResult<MenuResponse> result = menuService.update(id, updateMenuRequest);
            
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDataResult<>("Menü güncellenirken bir hata oluştu: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Tüm menüleri getir", description = "Tüm menülerin listesini döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getAll() {
        return ResponseEntity.ok(menuService.getAll());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Tüm menüleri sayfalı getir", description = "Tüm menüleri sayfalama ile döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuService.getAll(page, size));
    }

    @GetMapping("/active")
    @Operation(summary = "Aktif menüleri getir", description = "Aktif menüleri sayfalama ile döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuService.getAllActive(page, size));
    }

    @GetMapping("/available")
    @Operation(summary = "Mevcut menüleri getir", description = "Mevcut menü öğelerini döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getAllAvailable() {
        return ResponseEntity.ok(menuService.getAllAvailable());
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Kategoriye göre menüleri getir", description = "Belirtilen kategoriye ait menüleri döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuService.getByCategory(categoryId));
    }

    @GetMapping("/category/{categoryId}/active")
    @Operation(summary = "Kategoriye göre aktif menüleri getir", description = "Belirtilen kategoriye ait aktif menüleri sayfalama ile döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getByCategoryActive(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuService.getByCategoryActive(categoryId, page, size));
    }

    @GetMapping("/category/{categoryId}/available")
    @Operation(summary = "Kategoriye göre mevcut menüleri getir", description = "Belirtilen kategoriye ait mevcut menüleri döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getByCategoryAvailable(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuService.getByCategoryAvailable(categoryId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile menü getir", description = "Belirtilen ID'ye sahip menüyü getirir")
    public ResponseEntity<DataResult<MenuResponse>> getById(@PathVariable Long id) {
        DataResult<MenuResponse> result = menuService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/search")
    @Operation(summary = "İsme göre menü ara", description = "İsimde arama yaparak menüleri döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuService.searchByName(name, page, size));
    }

    @GetMapping("/price-range")
    @Operation(summary = "Fiyat aralığına göre menü getir", description = "Belirtilen fiyat aralığındaki menüleri döndürür")
    public ResponseEntity<DataResult<List<MenuResponse>>> getByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(menuService.getByPriceRange(minPrice, maxPrice));
    }

    @PostMapping
    @Operation(
        summary = "Menü ekle", 
        description = "Yeni bir menü ekler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuResponse>> add(@Valid @RequestBody CreateMenuRequest createMenuRequest) {
        DataResult<MenuResponse> result = menuService.add(createMenuRequest);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Menü güncelle", 
        description = "Var olan bir menüyü günceller",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<MenuResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateMenuRequest updateMenuRequest) {
        DataResult<MenuResponse> result = menuService.update(id, updateMenuRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menü sil", 
        description = "Belirtilen ID'ye sahip menüyü siler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result result = menuService.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/activate/{id}")
    @Operation(
        summary = "Menü aktifleştir", 
        description = "Belirtilen ID'ye sahip menüyü aktifleştirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> activate(@PathVariable Long id) {
        Result result = menuService.activate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(
        summary = "Menü deaktifleştir", 
        description = "Belirtilen ID'ye sahip menüyü deaktifleştirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> deactivate(@PathVariable Long id) {
        Result result = menuService.deactivate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/make-available/{id}")
    @Operation(
        summary = "Menüyü mevcut yap", 
        description = "Belirtilen ID'ye sahip menüyü mevcut duruma getirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> makeAvailable(@PathVariable Long id) {
        Result result = menuService.makeAvailable(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/make-unavailable/{id}")
    @Operation(
        summary = "Menüyü mevcut değil yap", 
        description = "Belirtilen ID'ye sahip menüyü mevcut değil duruma getirir",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> makeUnavailable(@PathVariable Long id) {
        Result result = menuService.makeUnavailable(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
} 