package in.chill.main.services;

import in.chill.main.dto.LoginRequest;
import in.chill.main.dto.RegisterRequest;
import in.chill.main.dto.AuthResponse;
import in.chill.main.dto.UserResponse;
import in.chill.main.entity.User;
import java.util.Optional;

public interface UserService {
    
    AuthResponse register(RegisterRequest registerRequest);
    
    AuthResponse login(LoginRequest loginRequest);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long userId);
    
    UserResponse getUserProfile(Long userId);
    
    boolean existsByEmail(String email);
}
