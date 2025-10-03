package in.chill.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.chill.main.entity.Registration;
import in.chill.main.services.RegistrationService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;
    
    @GetMapping("/registrations")
    public List<Registration> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }
    
    @GetMapping("/registrations/{id}")
    public ResponseEntity<Registration> getRegistrationById(@PathVariable int id) {
        Registration registration = registrationService.getRegistrationById(id).orElse(null);
        if (registration != null) {
            return ResponseEntity.ok(registration);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/registrations")
    public Registration createRegistration(@RequestBody Registration registration) {
        return registrationService.createRegistration(registration);
    }
    
    @PutMapping("/registrations/{id}")
    public ResponseEntity<Registration> updateRegistration(@PathVariable int id, @RequestBody Registration registration) {
        try {
            Registration updatedRegistration = registrationService.updateRegistration(id, registration);
            return ResponseEntity.ok(updatedRegistration);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/registrations/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable int id) {
        try {
            System.out.println("DELETE request received for registration ID: " + id);
            registrationService.deleteRegistration(id);
            System.out.println("Registration deleted successfully: " + id);
            return ResponseEntity.ok("Registration deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting registration: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
