package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.JobPositionService;
import com.demirciyazilim.business.dtos.job.requests.CreateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.requests.UpdateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
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
@RequestMapping("/api/v1/jobs")
@AllArgsConstructor
@Tag(name = "Jobs", description = "Kariyer - İş İlanları API")
@CrossOrigin
public class JobPositionsController {

    private final JobPositionService jobPositionService;

    @GetMapping
    @Operation(summary = "Aktif ilanları getir", description = "Aktif iş ilanlarını sıralı döndürür")
    public ResponseEntity<DataResult<List<JobPositionResponse>>> getActive() {
        return ResponseEntity.ok(jobPositionService.getActive());
    }

    @GetMapping("/all")
    @Operation(summary = "Tüm ilanları getir", description = "Tüm iş ilanlarını sıralı döndürür", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<List<JobPositionResponse>>> getAll() {
        return ResponseEntity.ok(jobPositionService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "İlan detay", description = "İlanı ID ile getirir")
    public ResponseEntity<DataResult<JobPositionResponse>> getById(@PathVariable Long id) {
        DataResult<JobPositionResponse> result = jobPositionService.getById(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    @Operation(summary = "İlan ekle", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<JobPositionResponse>> add(@Valid @RequestBody CreateJobPositionRequest request) {
        DataResult<JobPositionResponse> result = jobPositionService.add(request);
        if (result.isSuccess()) return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "İlan güncelle", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<JobPositionResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateJobPositionRequest request) {
        DataResult<JobPositionResponse> result = jobPositionService.update(id, request);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "İlanı aktifleştir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Result> activate(@PathVariable Long id) {
        Result result = jobPositionService.activate(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "İlanı pasifleştir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Result> deactivate(@PathVariable Long id) {
        Result result = jobPositionService.deactivate(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "İlan sil", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result result = jobPositionService.delete(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}



