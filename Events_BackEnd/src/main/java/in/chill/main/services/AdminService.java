package in.chill.main.services;

import in.chill.main.dto.AdminRequest;
import in.chill.main.dto.AdminResponse;
import in.chill.main.dto.AuthResponse;
import in.chill.main.dto.LoginRequest;
import in.chill.main.dto.UserResponse;
import in.chill.main.entity.Admin;
import in.chill.main.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Admin login - only existing admins can login
     * @param loginRequest Login credentials
     * @return Authentication response
     */
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Validate admin email pattern
            if (!Admin.isValidAdminEmail(loginRequest.getEmail())) {
                return new AuthResponse(null, null, "Invalid admin email format. Must end with @admin.org");
            }
            
            Optional<Admin> adminOpt = adminRepository.findByEmail(loginRequest.getEmail());
            
            if (adminOpt.isEmpty()) {
                return new AuthResponse(null, null, "Admin not found. Contact support for access.");
            }
            
            Admin admin = adminOpt.get();
            
            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                return new AuthResponse(null, null, "Invalid password");
            }
            
            // Create UserResponse from Admin
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(admin.getAdminId());
            userResponse.setEmail(admin.getEmail());
            userResponse.setFirstName(admin.getName()); // Admin has name field, not firstName
            userResponse.setLastName(""); // Admin doesn't have lastName
            userResponse.setRole(admin.getRole());
            userResponse.setCollege("N/A"); // Admins don't have college
            userResponse.setContact("N/A"); // Admins don't have contact in this model
            
            // For now, return success without JWT token (will implement later)
            return new AuthResponse("admin-temp-token", userResponse, "Admin login successful");
            
        } catch (Exception e) {
            return new AuthResponse(null, null, "Login failed: " + e.getMessage());
        }
    }
    
    /**
     * Get admin by email
     * @param email Admin email
     * @return Admin if found
     */
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
    /**
     * Create new admin (for internal use only)
     * @param admin Admin to create
     * @return Created admin
     */
    public Admin createAdmin(Admin admin) {
        // Validate email pattern
        if (!Admin.isValidAdminEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Admin email must end with @admin.org");
        }
        
        // Check if admin already exists
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Admin with this email already exists");
        }
        
        // Hash password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        
        return adminRepository.save(admin);
    }
    
    /**
     * Get all admins (returns AdminResponse without passwords)
     * @return List of all admins
     */
    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
            .map(admin -> new AdminResponse(
                admin.getAdminId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
            ))
            .collect(Collectors.toList());
    }
    
    /**
     * Get admin by ID (returns AdminResponse without password)
     * @param adminId Admin ID
     * @return AdminResponse if found
     */
    public Optional<AdminResponse> getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
            .map(admin -> new AdminResponse(
                admin.getAdminId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
            ));
    }
    
    /**
     * Create new admin with bcrypt password hashing
     * @param adminRequest Admin data
     * @return Created admin (without password)
     */
    public AdminResponse createAdminFromRequest(AdminRequest adminRequest) {
        // Check if admin already exists
        if (adminRepository.existsByEmail(adminRequest.getEmail())) {
            throw new IllegalArgumentException("Admin with this email already exists");
        }
        
        // Create new admin entity
        Admin admin = new Admin();
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword())); // Bcrypt hash
        admin.setRole(adminRequest.getRole() != null ? adminRequest.getRole() : "ADMIN");
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        
        Admin savedAdmin = adminRepository.save(admin);
        
        // Return AdminResponse (without password)
        return new AdminResponse(
            savedAdmin.getAdminId(),
            savedAdmin.getName(),
            savedAdmin.getEmail(),
            savedAdmin.getRole(),
            savedAdmin.getCreatedAt(),
            savedAdmin.getUpdatedAt()
        );
    }
    
    /**
     * Update existing admin
     * @param adminId Admin ID
     * @param adminRequest Updated admin data
     * @return Updated admin (without password)
     */
    public AdminResponse updateAdmin(Long adminId, AdminRequest adminRequest) {
        Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        
        // Update fields
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        
        // Only update password if provided and not empty
        if (adminRequest.getPassword() != null && !adminRequest.getPassword().trim().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminRequest.getPassword())); // Bcrypt hash
        }
        
        if (adminRequest.getRole() != null) {
            admin.setRole(adminRequest.getRole());
        }
        
        admin.setUpdatedAt(LocalDateTime.now());
        
        Admin updatedAdmin = adminRepository.save(admin);
        
        // Return AdminResponse (without password)
        return new AdminResponse(
            updatedAdmin.getAdminId(),
            updatedAdmin.getName(),
            updatedAdmin.getEmail(),
            updatedAdmin.getRole(),
            updatedAdmin.getCreatedAt(),
            updatedAdmin.getUpdatedAt()
        );
    }
    
    /**
     * Delete admin
     * @param adminId Admin ID
     */
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new IllegalArgumentException("Admin not found");
        }
        adminRepository.deleteById(adminId);
    }
}