package com.demirciyazilim.core.utilities.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.ErrorDataResult;
import com.demirciyazilim.core.utilities.results.SuccessDataResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public DataResult<String> uploadFile(MultipartFile file, String folder) {
        try {
            validateFile(file);

            // İçerik tipine göre resource_type belirle (görseller -> image, dökümanlar -> raw)
            String contentType = file.getContentType();
            boolean isDocument = contentType != null && (
                    contentType.equals("application/pdf") ||
                    contentType.equals("application/msword") ||
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            );
            String resourceType = isDocument ? "raw" : "image";

            // Public ID: dökümanlarda uzantıyı koruyalım ki URL sonu .pdf/.docx olsun
            String uuid = UUID.randomUUID().toString();
            String publicIdBase = uuid;
            String originalName = file.getOriginalFilename();
            String ext = null;
            if (originalName != null) {
                int dot = originalName.lastIndexOf('.');
                if (dot > -1 && dot < originalName.length() - 1) {
                    ext = originalName.substring(dot + 1).toLowerCase();
                }
            }

            Map<String, Object> options = ObjectUtils.asMap(
                    "public_id", publicIdBase,
                    "folder", folder,
                    "resource_type", resourceType
            );
            // Dökümanları public teslim için 'type' varsayılan 'upload' kalsın (PDF delivery izni açık olmalı)
            if (isDocument && ext != null) {
                options.put("format", ext); // İçerik tipini/uzantıyı garanti et
            }

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            
            // Cloudinary'den dönen URL'i al
            String fileUrl = uploadResult.get("secure_url").toString();
            return new SuccessDataResult<>(fileUrl, "Dosya başarıyla yüklendi");
        } catch (IllegalArgumentException e) {
            return new ErrorDataResult<>(e.getMessage());
        } catch (IOException e) {
            return new ErrorDataResult<>("Dosya yükleme sırasında bir hata oluştu: " + e.getMessage());
        } catch (Exception e) {
            return new ErrorDataResult<>("Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");
        }
        
        // Dosya boyutu kontrolü (20MB)
        if (file.getSize() > 20 * 1024 * 1024) {
            throw new IllegalArgumentException("Dosya boyutu 20MB'dan büyük olamaz");
        }
        
        // Dosya türü kontrolü (görsel + döküman)
        String contentType = file.getContentType();
        boolean isImage = contentType != null && (
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp")
        );
        boolean isDocument = contentType != null && (
                contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
        if (!isImage && !isDocument) {
            throw new IllegalArgumentException("Sadece JPEG, PNG, GIF, WEBP, PDF, DOC ve DOCX formatları desteklenmektedir");
        }
    }
} 