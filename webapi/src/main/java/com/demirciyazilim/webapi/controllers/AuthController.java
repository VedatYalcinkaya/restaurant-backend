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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "Kimlik Doğrulama API")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yapar")
    public ResponseEntity<DataResult<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        DataResult<JwtAuthResponse> result = authService.login(loginRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @PostMapping("/register")
    @Operation(summary = "Kullanıcı kaydı", description = "Yeni kullanıcı kaydı oluşturur")
    public ResponseEntity<DataResult<JwtAuthResponse>> register(@Valid @RequestBody CreateUserRequest registerRequest) {
        DataResult<JwtAuthResponse> result = authService.register(registerRequest);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @GetMapping("/validate")
    @Operation(
        summary = "Token doğrulama", 
        description = "JWT token'ın geçerliliğini kontrol eder ve kullanıcı bilgilerini döndürür. Token'ı 'Bearer {token}' formatında gönderin.",
        security = @SecurityRequirement(name = "bearer-key")
    )
    public ResponseEntity<DataResult<UserInfoResponse>> validateToken(@RequestHeader(name = "Authorization", required = true) String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            DataResult<UserInfoResponse> result = authService.validateToken(token);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>("Token 'Bearer {token}' formatında olmalıdır"));
    }
    
    @PostMapping("/refresh")
    @Operation(
        summary = "Token yenileme", 
        description = "Refresh token kullanarak yeni bir erişim token'ı oluşturur"
    )
    public ResponseEntity<DataResult<JwtAuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        DataResult<JwtAuthResponse> result = authService.refreshToken(refreshTokenRequest);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @PostMapping("/logout")
    @Operation(
        summary = "Çıkış yapma", 
        description = "Kullanıcı oturumunu sonlandırır ve refresh token'ı geçersiz kılar",
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