package com.demirciyazilim.business.dtos.auth.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
} 