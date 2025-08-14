package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.JobApplicationService;
import com.demirciyazilim.business.dtos.jobapplication.requests.CreateJobApplicationRequest;
import com.demirciyazilim.business.dtos.jobapplication.requests.UpdateJobApplicationStatusRequest;
import com.demirciyazilim.business.dtos.jobapplication.responses.JobApplicationResponse;
import com.demirciyazilim.core.utilities.file.CloudinaryService;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.JobApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-applications")
@AllArgsConstructor
@Tag(name = "Job Applications", description = "Kariyer - İş Başvuruları API")
@CrossOrigin
public class JobApplicationsController {

    private final JobApplicationService jobApplicationService;
    private final CloudinaryService cloudinaryService;

    @PostMapping
    @Operation(summary = "İş başvurusu yap")
    public ResponseEntity<DataResult<JobApplicationResponse>> apply(@Valid @RequestBody CreateJobApplicationRequest request) {
        DataResult<JobApplicationResponse> result = jobApplicationService.apply(request);
        if (result.isSuccess()) return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping(value = "/apply-with-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "CV ile iş başvurusu yap", description = "CV dosyası yükleyerek iş başvurusu yapar",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)))
    public ResponseEntity<DataResult<JobApplicationResponse>> applyWithResume(
            @RequestParam("applicationData") String applicationDataJson,
            @RequestParam(value = "resume", required = false) MultipartFile resume) {
        try {
            // JSON parse
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            CreateJobApplicationRequest request = mapper.readValue(applicationDataJson, CreateJobApplicationRequest.class);

            if (resume != null && !resume.isEmpty()) {
                DataResult<String> upload = cloudinaryService.uploadFile(resume, "careers/resumes");
                if (!upload.isSuccess()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new com.demirciyazilim.core.utilities.results.ErrorDataResult<>(upload.getMessage()));
                }
                // Yüklenen dosya URL'ini isteğe yaz ki DB'ye kaydedilsin
                request.setResumeUrl(upload.getData());
            }

            DataResult<JobApplicationResponse> result = jobApplicationService.apply(request);
            return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new com.demirciyazilim.core.utilities.results.ErrorDataResult<>("Başvuru sırasında hata: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Başvuruları getir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<List<JobApplicationResponse>>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobApplicationService.getAll(page, size));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Duruma göre başvuruları getir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<List<JobApplicationResponse>>> getByStatus(@PathVariable JobApplicationStatus status,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobApplicationService.getByStatus(status, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Başvuru detay", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<JobApplicationResponse>> getById(@PathVariable Long id) {
        DataResult<JobApplicationResponse> result = jobApplicationService.getById(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Başvuru durumu güncelle", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Result> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateJobApplicationStatusRequest request) {
        Result result = jobApplicationService.updateStatus(id, request);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Başvuru sil", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result result = jobApplicationService.delete(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }
}



