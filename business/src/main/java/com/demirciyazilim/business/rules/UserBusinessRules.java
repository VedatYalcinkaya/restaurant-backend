package com.demirciyazilim.business.rules;

import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserBusinessRules {
    
    private final UserRepository userRepository;
    
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    public void checkIfUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(Messages.USER_NOT_FOUND);
        }
    }
    
    public void checkIfUserExistsByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new BusinessException(Messages.USER_NOT_FOUND);
        }
    }
    
    public void checkIfUsernameNotExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(Messages.USERNAME_ALREADY_EXISTS);
        }
    }
    
    public void checkIfEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(Messages.EMAIL_ALREADY_EXISTS);
        }
    }
    
    public void checkIfUsernameIsValid(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(Messages.USERNAME_EMPTY);
        }
        
        if (username.length() < 3) {
            throw new BusinessException(Messages.USERNAME_MIN_LENGTH);
        }
        
        if (username.length() > 50) {
            throw new BusinessException(Messages.USERNAME_MAX_LENGTH);
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new BusinessException(Messages.USERNAME_PATTERN_NOT_VALID);
        }
    }
    
    public void checkIfEmailIsValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException(Messages.EMAIL_EMPTY);
        }
        
        if (!pattern.matcher(email).matches()) {
            throw new BusinessException(Messages.EMAIL_PATTERN_NOT_VALID);
        }
        
        if (email.length() > 100) {
            throw new BusinessException(Messages.EMAIL_MAX_LENGTH);
        }
    }
    
    public void checkIfPasswordIsValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(Messages.PASSWORD_EMPTY);
        }
        
        if (password.length() < 6) {
            throw new BusinessException(Messages.PASSWORD_MIN_LENGTH);
        }
        
        if (password.length() > 100) {
            throw new BusinessException(Messages.PASSWORD_MAX_LENGTH);
        }
    }
} 