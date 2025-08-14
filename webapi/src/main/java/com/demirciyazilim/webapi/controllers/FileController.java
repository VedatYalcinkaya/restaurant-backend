package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.core.utilities.file.CloudinaryService;
import com.demirciyazilim.core.utilities.results.DataResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/files")
@AllArgsConstructor
@Tag(name = "File", description = "File Upload API")
public class FileController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload/reference", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Referans resmi yükle", 
        description = "Cloudinary'ye referans resmi yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        )
    )
    public ResponseEntity<DataResult<String>> uploadReferenceImage(
            @Parameter(description = "Yüklenecek dosya", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        DataResult<String> result = cloudinaryService.uploadFile(file, "references");
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @PostMapping(value = "/upload/blog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Blog resmi yükle", 
        description = "Cloudinary'ye blog resmi yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        )
    )
    public ResponseEntity<DataResult<String>> uploadBlogImage(
            @Parameter(description = "Yüklenecek dosya", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        DataResult<String> result = cloudinaryService.uploadFile(file, "blogs");
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @PostMapping(value = "/upload/content-block", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "İçerik bloğu resmi yükle", 
        description = "Cloudinary'ye içerik bloğu resmi yükler",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        )
    )
    public ResponseEntity<DataResult<String>> uploadContentBlockImage(
            @Parameter(description = "Yüklenecek dosya", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        DataResult<String> result = cloudinaryService.uploadFile(file, "content-blocks");
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping(value = "/proxy")
    @Operation(summary = "Cloudinary dosya proxy", description = "Verilen URL'deki dosyayı doğru header'larla iletir (inline görüntüleme)")
    public ResponseEntity<byte[]> proxy(@RequestParam("url") String url) {
        try {
            // Authenticated (signed) URL gerekiyorsa transform kısmına imza ekleyelim
            // Cloudinary raw authenticated dosyalar için query'de 'signature' ve 'api_key' gerekir
            // Basitçe gelen URL authenticated değilse direkt deneyelim; 401 dönerse kullanıcı indirme linkini kullansın
            RestTemplate rest = new RestTemplate();
            HttpHeaders h = new HttpHeaders();
            h.set("User-Agent", "Mozilla/5.0");
            ResponseEntity<byte[]> resp = rest.exchange(url, HttpMethod.GET, new HttpEntity<>(h), byte[].class);

            MediaType ct = resp.getHeaders().getContentType();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(ct != null ? ct : MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline");
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
            return new ResponseEntity<>(resp.getBody(), headers, HttpStatus.OK);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
} 