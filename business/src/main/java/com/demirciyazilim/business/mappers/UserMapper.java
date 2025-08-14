package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.user.requests.CreateUserRequest;
import com.demirciyazilim.business.dtos.user.requests.UpdateUserRequest;
import com.demirciyazilim.business.dtos.user.responses.UserResponse;
import com.demirciyazilim.entities.User;
import com.demirciyazilim.entities.enums.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setRole(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.USER);
        user.setActive(request.isActive());
        return user;
    }

    public User toEntity(UpdateUserRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setRole(Role.valueOf(request.getRole()));
        user.setActive(request.isActive());
        return user;
    }

    public UserResponse toResponse(User entity) {
        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setUsername(entity.getUsername());
        response.setEmail(entity.getEmail());
        
        // Tam adı parçalara ayırma
        String fullName = entity.getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.split(" ", 2);
            response.setFirstName(nameParts[0]);
            response.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        }
        
        response.setRole(entity.getRole().name());
        response.setActive(entity.isActive());
        response.setLastLoginDate(entity.getLastLogin());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public List<UserResponse> toResponseList(List<User> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(UpdateUserRequest request, User entity) {
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(request.getPassword());
        }
        entity.setFullName(request.getFirstName() + " " + request.getLastName());
        entity.setRole(Role.valueOf(request.getRole()));
        entity.setActive(request.isActive());
    }
} 