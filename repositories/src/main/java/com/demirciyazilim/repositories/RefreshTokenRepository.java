package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.RefreshToken;
import com.demirciyazilim.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUserAndBlacklistedFalse(User user);
    
    @Modifying
    @Query("UPDATE RefreshToken r SET r.blacklisted = true WHERE r.user = ?1 AND r.blacklisted = false")
    void invalidateAllUserTokens(User user);
    
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiryDate < ?1")
    void deleteByExpiryDateBefore(Instant now);
    
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user = ?1")
    void deleteByUser(User user);
    
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.blacklisted = true")
    void deleteByBlacklistedTrue();
} 