package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.RefreshToken;
import com.demirciyazilim.entities.User;

import java.util.Optional;

public interface RefreshTokenService {
    
    DataResult<RefreshToken> createRefreshToken(User user);
    
    DataResult<RefreshToken> verifyExpiration(RefreshToken token);
    
    Result deleteByUserId(Long userId);
    
    DataResult<RefreshToken> findByToken(String token);
    
    Result revokeToken(String token);
    
    Result cleanupExpiredTokens();
} 