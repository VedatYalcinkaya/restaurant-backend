package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.auth.requests.LoginRequest;
import com.demirciyazilim.business.dtos.auth.requests.RefreshTokenRequest;
import com.demirciyazilim.business.dtos.auth.responses.JwtAuthResponse;
import com.demirciyazilim.business.dtos.auth.responses.UserInfoResponse;
import com.demirciyazilim.business.dtos.user.requests.CreateUserRequest;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;

public interface AuthService {
    
    DataResult<JwtAuthResponse> login(LoginRequest loginRequest);
    
    DataResult<JwtAuthResponse> register(CreateUserRequest registerRequest);
    
    DataResult<UserInfoResponse> validateToken(String token);
    
    DataResult<JwtAuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
    
    Result logout(String refreshToken);
} 