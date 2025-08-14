package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.ReservationService;
import com.demirciyazilim.business.dtos.reservation.requests.CreateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationStatusRequest;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationResponse;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationSummaryResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
@Tag(name = "Reservations", description = "Rezervasyon API")
@CrossOrigin
public class ReservationsController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Rezervasyon oluştur", description = "Yeni bir rezervasyon oluşturur")
    public ResponseEntity<DataResult<ReservationResponse>> createReservation(@Valid @RequestBody CreateReservationRequest createReservationRequest) {
        DataResult<ReservationResponse> result = reservationService.add(createReservationRequest);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile rezervasyon getir", description = "Belirtilen ID'ye sahip rezervasyonu getirir")
    public ResponseEntity<DataResult<ReservationResponse>> getById(@PathVariable Long id) {
        DataResult<ReservationResponse> result = reservationService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping
    @Operation(
        summary = "Tüm rezervasyonları getir", 
        description = "Tüm rezervasyonları sayfalama ile döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getAll(page, size));
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Duruma göre rezervasyonları getir", 
        description = "Belirtilen duruma sahip rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByStatus(
            @PathVariable ReservationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getByStatus(status, page, size));
    }

    @GetMapping("/date/{date}")
    @Operation(
        summary = "Tarihe göre rezervasyonları getir", 
        description = "Belirtilen tarihteki rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reservationService.getByDate(date));
    }

    @GetMapping("/date/{date}/status/{status}")
    @Operation(
        summary = "Tarih ve duruma göre rezervasyonları getir", 
        description = "Belirtilen tarih ve duruma sahip rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByDateAndStatus(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable ReservationStatus status) {
        return ResponseEntity.ok(reservationService.getByDateAndStatus(date, status));
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Tarih aralığına göre rezervasyonları getir", 
        description = "Belirtilen tarih aralığındaki rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reservationService.getByDateRange(startDate, endDate));
    }

    @GetMapping("/date-range/status/{status}")
    @Operation(
        summary = "Tarih aralığı ve duruma göre rezervasyonları getir", 
        description = "Belirtilen tarih aralığı ve duruma sahip rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByDateRangeAndStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PathVariable ReservationStatus status) {
        return ResponseEntity.ok(reservationService.getByDateRangeAndStatus(startDate, endDate, status));
    }

    @GetMapping("/customer/email/{email}")
    @Operation(
        summary = "Email'e göre rezervasyonları getir", 
        description = "Belirtilen email adresine sahip müşterinin rezervasyonlarını döndürür"
    )
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByCustomerEmail(@PathVariable String email) {
        return ResponseEntity.ok(reservationService.getByCustomerEmail(email));
    }

    @GetMapping("/customer/phone/{phone}")
    @Operation(
        summary = "Telefona göre rezervasyonları getir", 
        description = "Belirtilen telefon numarasına sahip müşterinin rezervasyonlarını döndürür"
    )
    public ResponseEntity<DataResult<List<ReservationResponse>>> getByCustomerPhone(@PathVariable String phone) {
        return ResponseEntity.ok(reservationService.getByCustomerPhone(phone));
    }

    @GetMapping("/today")
    @Operation(
        summary = "Bugünkü rezervasyonları getir", 
        description = "Bugünkü rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getTodayReservations() {
        return ResponseEntity.ok(reservationService.getTodayReservations());
    }

    @GetMapping("/tomorrow")
    @Operation(
        summary = "Yarınki rezervasyonları getir", 
        description = "Yarınki rezervasyonları döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getTomorrowReservations() {
        return ResponseEntity.ok(reservationService.getTomorrowReservations());
    }

    @GetMapping("/upcoming")
    @Operation(
        summary = "Gelecek rezervasyonları getir", 
        description = "Gelecek rezervasyonları sayfalama ile döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getUpcomingReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getUpcomingReservations(page, size));
    }

    @GetMapping("/past")
    @Operation(
        summary = "Geçmiş rezervasyonları getir", 
        description = "Geçmiş rezervasyonları sayfalama ile döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<List<ReservationResponse>>> getPastReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getPastReservations(page, size));
    }

    @GetMapping("/summary")
    @Operation(
        summary = "Rezervasyon özeti getir", 
        description = "Rezervasyon istatistiklerini döndürür",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<ReservationSummaryResponse>> getReservationSummary() {
        return ResponseEntity.ok(reservationService.getReservationSummary());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Rezervasyon güncelle", 
        description = "Var olan bir rezervasyonu günceller",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<ReservationResponse>> updateReservation(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateReservationRequest updateReservationRequest) {
        DataResult<ReservationResponse> result = reservationService.update(id, updateReservationRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Rezervasyon durumu güncelle", 
        description = "Rezervasyon durumunu günceller",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<ReservationResponse>> updateReservationStatus(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateReservationStatusRequest updateReservationStatusRequest) {
        DataResult<ReservationResponse> result = reservationService.updateStatus(id, updateReservationStatusRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/{id}/confirm")
    @Operation(
        summary = "Rezervasyon onayla", 
        description = "Rezervasyonu onaylar",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> confirmReservation(@PathVariable Long id) {
        Result result = reservationService.confirmReservation(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
        summary = "Rezervasyon iptal et", 
        description = "Rezervasyonu iptal eder",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> cancelReservation(@PathVariable Long id) {
        Result result = reservationService.cancelReservation(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/{id}/complete")
    @Operation(
        summary = "Rezervasyon tamamla", 
        description = "Rezervasyonu tamamlar",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> completeReservation(@PathVariable Long id) {
        Result result = reservationService.completeReservation(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/{id}/no-show")
    @Operation(
        summary = "Rezervasyon gelmedi olarak işaretle", 
        description = "Rezervasyonu 'gelmedi' olarak işaretler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> markAsNoShow(@PathVariable Long id) {
        Result result = reservationService.markAsNoShow(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Rezervasyon sil", 
        description = "Belirtilen ID'ye sahip rezervasyonu siler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> deleteReservation(@PathVariable Long id) {
        Result result = reservationService.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @DeleteMapping("/finalized")
    @Operation(
        summary = "Finale olmuş rezervasyonları toplu sil", 
        description = "Durumu COMPLETED, CANCELLED veya NO_SHOW olan tüm rezervasyonları siler",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> deleteAllFinalized() {
        Result result = reservationService.deleteAllFinalized();
        return ResponseEntity.ok(result);
    }
}
