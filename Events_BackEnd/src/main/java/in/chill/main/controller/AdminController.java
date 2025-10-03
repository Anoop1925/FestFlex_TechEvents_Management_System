package in.chill.main.controller;

import in.chill.main.dto.AdminRequest;
import in.chill.main.dto.AdminResponse;
import in.chill.main.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    /**
     * Get all admins (passwords excluded via AdminResponse DTO)
     */
    @GetMapping
    public List<AdminResponse> getAllAdmins() {
        System.out.println("GET request received for all admins");
        return adminService.getAllAdmins();
    }
    
    /**
     * Get admin by ID (password excluded)
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        System.out.println("GET request received for admin ID: " + id);
        return adminService.getAdminById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new admin (password will be bcrypt hashed)
     */
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest adminRequest) {
        System.out.println("POST request received to create admin: " + adminRequest.getEmail());
        try {
            AdminResponse createdAdmin = adminService.createAdminFromRequest(adminRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating admin: " + e.getMessage());
        }
    }
    
    /**
     * Update existing admin (password will be bcrypt hashed if provided)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody AdminRequest adminRequest) {
        System.out.println("PUT request received to update admin ID: " + id);
        try {
            AdminResponse updatedAdmin = adminService.updateAdmin(id, adminRequest);
            return ResponseEntity.ok(updatedAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating admin: " + e.getMessage());
        }
    }
    
    /**
     * Delete admin
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        System.out.println("DELETE request received for admin ID: " + id);
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting admin: " + e.getMessage());
        }
    }
}
