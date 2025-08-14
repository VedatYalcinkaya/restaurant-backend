package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.UserService;
import com.demirciyazilim.business.constants.Messages;
import com.demirciyazilim.business.rules.UserBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.User;
import com.demirciyazilim.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserManager implements UserService {
    
    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public DataResult<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        return new SuccessDataResult<>(users, Messages.USERS_LISTED_SUCCESSFULLY);
    }
    
    @Override
    public DataResult<User> getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new ErrorDataResult<>(Messages.USER_NOT_FOUND);
        }
        return new SuccessDataResult<>(user.get(), Messages.USER_FETCHED_SUCCESSFULLY);
    }
    
    @Override
    public DataResult<User> getByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ErrorDataResult<>(Messages.USER_NOT_FOUND);
        }
        return new SuccessDataResult<>(user.get(), Messages.USER_FETCHED_SUCCESSFULLY);
    }
    
    @Override
    public DataResult<User> getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return new ErrorDataResult<>(Messages.USER_NOT_FOUND);
        }
        return new SuccessDataResult<>(user.get(), Messages.USER_FETCHED_SUCCESSFULLY);
    }
    
    @Override
    public DataResult<User> add(User user) {
        userBusinessRules.checkIfUsernameNotExists(user.getUsername());
        userBusinessRules.checkIfEmailNotExists(user.getEmail());
        userBusinessRules.checkIfUsernameIsValid(user.getUsername());
        userBusinessRules.checkIfEmailIsValid(user.getEmail());
        userBusinessRules.checkIfPasswordIsValid(user.getPassword());
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        User savedUser = userRepository.save(user);
        return new SuccessDataResult<>(savedUser, Messages.USER_ADDED_SUCCESSFULLY);
    }
    
    @Override
    public DataResult<User> update(User user) {
        userBusinessRules.checkIfUserExists(user.getId());
        
        User existingUser = userRepository.findById(user.getId()).get();
        
        if (!existingUser.getUsername().equals(user.getUsername())) {
            userBusinessRules.checkIfUsernameNotExists(user.getUsername());
            userBusinessRules.checkIfUsernameIsValid(user.getUsername());
        }
        
        if (!existingUser.getEmail().equals(user.getEmail())) {
            userBusinessRules.checkIfEmailNotExists(user.getEmail());
            userBusinessRules.checkIfEmailIsValid(user.getEmail());
        }
        
        user.setPassword(existingUser.getPassword());
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return new SuccessDataResult<>(updatedUser, Messages.USER_UPDATED_SUCCESSFULLY);
    }
    
    @Override
    public Result delete(Long id) {
        userBusinessRules.checkIfUserExists(id);
        userRepository.deleteById(id);
        return new SuccessResult(Messages.USER_DELETED_SUCCESSFULLY);
    }
    
    @Override
    public Result activate(Long id) {
        userBusinessRules.checkIfUserExists(id);
        User user = userRepository.findById(id).get();
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new SuccessResult(Messages.USER_ACTIVATED_SUCCESSFULLY);
    }
    
    @Override
    public Result deactivate(Long id) {
        userBusinessRules.checkIfUserExists(id);
        User user = userRepository.findById(id).get();
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new SuccessResult(Messages.USER_DEACTIVATED_SUCCESSFULLY);
    }
    
    @Override
    public Result changePassword(Long id, String oldPassword, String newPassword) {
        userBusinessRules.checkIfUserExists(id);
        userBusinessRules.checkIfPasswordIsValid(newPassword);
        
        User user = userRepository.findById(id).get();
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return new ErrorResult(Messages.OLD_PASSWORD_INCORRECT);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new SuccessResult(Messages.PASSWORD_CHANGED_SUCCESSFULLY);
    }
    
    @Override
    public Result updateLastLogin(String username) {
        userBusinessRules.checkIfUserExistsByUsername(username);
        User user = userRepository.findByUsername(username).get();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new SuccessResult();
    }
    
    @Override
    public Result existsByUsername(String username) {
        boolean exists = userRepository.existsByUsername(username);
        return exists ? new SuccessResult() : new ErrorResult();
    }
    
    @Override
    public Result existsByEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        return exists ? new SuccessResult() : new ErrorResult();
    }
} 