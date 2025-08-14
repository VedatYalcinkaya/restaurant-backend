package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.User;

import java.util.List;

public interface UserService {
    
    DataResult<List<User>> getAll();
    
    DataResult<User> getById(Long id);
    
    DataResult<User> getByUsername(String username);
    
    DataResult<User> getByEmail(String email);
    
    DataResult<User> add(User user);
    
    DataResult<User> update(User user);
    
    Result delete(Long id);
    
    Result activate(Long id);
    
    Result deactivate(Long id);
    
    Result changePassword(Long id, String oldPassword, String newPassword);
    
    Result updateLastLogin(String username);
    
    Result existsByUsername(String username);
    
    Result existsByEmail(String email);
} 