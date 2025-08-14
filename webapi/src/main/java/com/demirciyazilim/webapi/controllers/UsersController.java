package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.UserService;
import com.demirciyazilim.business.dtos.user.requests.ChangePasswordRequest;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "User", description = "Kullanıcı API")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin
public class UsersController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Tüm kullanıcıları getir", description = "Tüm kullanıcıların listesini döndürür")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult<List<User>>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile kullanıcı getir", description = "Belirtilen ID'ye sahip kullanıcıyı getirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<User>> getById(@PathVariable Long id) {
        DataResult<User> result = userService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Kullanıcı adı ile kullanıcı getir", description = "Belirtilen kullanıcı adına sahip kullanıcıyı getirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<User>> getByUsername(@PathVariable String username) {
        DataResult<User> result = userService.getByUsername(username);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "E-posta ile kullanıcı getir", description = "Belirtilen e-posta adresine sahip kullanıcıyı getirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<User>> getByEmail(@PathVariable String email) {
        DataResult<User> result = userService.getByEmail(email);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PutMapping
    @Operation(summary = "Kullanıcı güncelle", description = "Mevcut bir kullanıcıyı günceller")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<DataResult<User>> update(@RequestBody User user) {
        DataResult<User> result = userService.update(user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kullanıcı sil", description = "Belirtilen ID'ye sahip kullanıcıyı siler")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        Result result = userService.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Kullanıcı aktifleştir", description = "Belirtilen ID'ye sahip kullanıcıyı aktifleştirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> activate(@PathVariable Long id) {
        Result result = userService.activate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Kullanıcı deaktifleştir", description = "Belirtilen ID'ye sahip kullanıcıyı deaktifleştirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> deactivate(@PathVariable Long id) {
        Result result = userService.deactivate(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Şifre değiştir", description = "Kullanıcı şifresini değiştirir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'USER')")
    public ResponseEntity<Result> changePassword(@RequestBody ChangePasswordRequest request) {
        Result result = userService.changePassword(request.getId(), request.getOldPassword(), request.getNewPassword());
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PatchMapping("/update-last-login/{username}")
    @Operation(summary = "Son giriş zamanını güncelle", description = "Kullanıcının son giriş zamanını günceller")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Result> updateLastLogin(@PathVariable String username) {
        Result result = userService.updateLastLogin(username);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/exists/username/{username}")
    @Operation(summary = "Kullanıcı adı kontrolü", description = "Belirtilen kullanıcı adının var olup olmadığını kontrol eder")
    public ResponseEntity<Result> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "E-posta kontrolü", description = "Belirtilen e-posta adresinin var olup olmadığını kontrol eder")
    public ResponseEntity<Result> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
} 