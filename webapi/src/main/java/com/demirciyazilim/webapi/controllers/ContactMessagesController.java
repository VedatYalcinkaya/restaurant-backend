package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.ContactMessageService;
import com.demirciyazilim.business.dtos.contactmessage.requests.CreateContactMessageRequest;
import com.demirciyazilim.business.dtos.contactmessage.responses.ContactMessageResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.ContactMessageStatus;
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
@RequestMapping("/api/v1/contact-messages")
@AllArgsConstructor
@Tag(name = "Contact Messages", description = "İletişim Mesajları API")
@CrossOrigin
public class ContactMessagesController {

    private final ContactMessageService service;

    @PostMapping
    @Operation(summary = "İletişim formu gönder")
    public ResponseEntity<DataResult<ContactMessageResponse>> create(@Valid @RequestBody CreateContactMessageRequest request) {
        var result = service.createAndNotify(request);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping
    @Operation(summary = "Mesajları getir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<List<ContactMessageResponse>>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(page, size));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Duruma göre mesajları getir", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<DataResult<List<ContactMessageResponse>>> getByStatus(@PathVariable ContactMessageStatus status,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getByStatus(status, page, size));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mesajı okundu işaretle", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Result> markRead(@PathVariable Long id) {
        Result r = service.markRead(id);
        return ResponseEntity.status(r.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(r);
    }

    @PatchMapping("/{id}/reply")
    @Operation(summary = "Mesajı cevaplandı işaretle", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Result> reply(@PathVariable Long id, @RequestParam String adminNotes) {
        Result r = service.reply(id, adminNotes);
        return ResponseEntity.status(r.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(r);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Mesajı sil", security = @SecurityRequirement(name = "bearer-key"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result r = service.delete(id);
        return ResponseEntity.status(r.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(r);
    }
}


