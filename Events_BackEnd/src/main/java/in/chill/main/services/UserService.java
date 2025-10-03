package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import in.chill.main.dto.AuthResponse;
import in.chill.main.dto.LoginRequest;
import in.chill.main.dto.RegisterRequest;
import in.chill.main.dto.UserResponse;
import in.chill.main.entity.User;

public interface UserService {
    
    AuthResponse register(RegisterRequest registerRequest);
    
    AuthResponse login(LoginRequest loginRequest);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long userId);
    
    UserResponse getUserProfile(Long userId);
    
    boolean existsByEmail(String email);
    
    List<UserResponse> getAllUsers();
    
    void deleteUser(Long userId);
}
