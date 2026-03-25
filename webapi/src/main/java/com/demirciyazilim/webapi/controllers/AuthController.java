package com.demirciyazilim.webapi.controllers;

import com.demirciyazilim.business.abstracts.AuthService;
import com.demirciyazilim.business.dtos.auth.requests.LoginRequest;
import com.demirciyazilim.business.dtos.auth.requests.LogoutRequest;
import com.demirciyazilim.business.dtos.auth.requests.RefreshTokenRequest;
import com.demirciyazilim.business.dtos.auth.responses.JwtAuthResponse;
import com.demirciyazilim.business.dtos.auth.responses.UserInfoResponse;
import com.demirciyazilim.business.dtos.user.requests.CreateUserRequest;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.ErrorDataResult;
import com.demirciyazilim.core.utilities.results.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "Kimlik Dogrulama API")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Kullanici girisi", description = "Kullanici adi ve sifre ile giris yapar")
    public ResponseEntity<DataResult<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        DataResult<JwtAuthResponse> result = authService.login(loginRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Kullanici kaydi",
            description = "Yeni bir kullanici olusturur. Bu endpoint sadece ADMIN kullanicilar icindir.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult<JwtAuthResponse>> register(@Valid @RequestBody CreateUserRequest registerRequest) {
        DataResult<JwtAuthResponse> result = authService.register(registerRequest);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/validate")
    @Operation(
            summary = "Token dogrulama",
            description = "JWT token'in gecerliligini kontrol eder ve kullanici bilgilerini dondurur. Token'i 'Bearer {token}' formatinda gonderin.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    public ResponseEntity<DataResult<UserInfoResponse>> validateToken(
            @RequestHeader(name = "Authorization", required = true) String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            DataResult<UserInfoResponse> result = authService.validateToken(token);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>("Token 'Bearer {token}' formatinda olmalidir"));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Token yenileme",
            description = "Refresh token kullanarak yeni bir erisim token'i olusturur"
    )
    public ResponseEntity<DataResult<JwtAuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        DataResult<JwtAuthResponse> result = authService.refreshToken(refreshTokenRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Cikis yapma",
            description = "Kullanici oturumunu sonlandirir ve refresh token'i gecersiz kilar",
            security = @SecurityRequirement(name = "bearer-key")
    )
    public ResponseEntity<Result> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        Result result = authService.logout(logoutRequest.getRefreshToken());
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
