package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.RefreshTokenService;
import com.demirciyazilim.repositories.RefreshTokenRepository;
import com.demirciyazilim.repositories.UserRepository;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.RefreshToken;
import com.demirciyazilim.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenManager implements RefreshTokenService {
    
    @Value("${app.jwt.refresh-expiration-milliseconds}")
    private long refreshTokenDurationMs;
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public RefreshTokenManager(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public DataResult<RefreshToken> createRefreshToken(User user) {
        try {
            // Önce kullanıcının tüm eski tokenlarını sil
            refreshTokenRepository.deleteByUser(user);
            
            // Yeni token oluştur
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                    .blacklisted(false)
                    .build();
            
            refreshToken = refreshTokenRepository.save(refreshToken);
            
            return new SuccessDataResult<>(refreshToken, "Refresh token başarıyla oluşturuldu");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Refresh token oluşturulurken hata: " + e.getMessage());
        }
    }
    
    @Override
    public DataResult<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.isBlacklisted()) {
            return new ErrorDataResult<>(null, "Token geçersiz kılınmış");
        }
        
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            return new ErrorDataResult<>(null, "Token süresi dolmuş. Lütfen tekrar giriş yapın");
        }
        
        return new SuccessDataResult<>(token, "Token geçerli");
    }
    
    @Override
    @Transactional
    public Result deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ErrorResult("Kullanıcı bulunamadı");
        }
        
        refreshTokenRepository.deleteByUser(user);
        return new SuccessResult("Kullanıcının tüm refresh token'ları silindi");
    }
    
    @Override
    public DataResult<RefreshToken> findByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            return new ErrorDataResult<>(null, "Token bulunamadı");
        }
        
        return new SuccessDataResult<>(refreshToken.get(), "Token bulundu");
    }
    
    @Override
    @Transactional
    public Result revokeToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            return new ErrorResult("Token bulunamadı");
        }
        
        RefreshToken tokenToRevoke = refreshToken.get();
        tokenToRevoke.setBlacklisted(true);
        refreshTokenRepository.save(tokenToRevoke);
        
        return new SuccessResult("Token başarıyla geçersiz kılındı");
    }
    
    @Override
    @Transactional
    public Result cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        refreshTokenRepository.deleteByBlacklistedTrue();
        return new SuccessResult("Süresi dolmuş ve geçersiz kılınmış token'lar temizlendi");
    }
    
    @Scheduled(cron = "0 0 * * * *") // Her saat başı
    @Transactional
    public void scheduledCleanup() {
        cleanupExpiredTokens();
    }
} 