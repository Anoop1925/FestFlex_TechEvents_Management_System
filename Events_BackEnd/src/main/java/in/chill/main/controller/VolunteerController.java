package in.chill.main.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import in.chill.main.dto.VolunteerDTO;
import in.chill.main.entity.Volunteer;
import in.chill.main.services.VolunteerService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VolunteerController {
    
    @Autowired
    private VolunteerService volunteerService;
    
    @GetMapping("/volunteers")
    public List<VolunteerDTO> getAllVolunteers() {
        return volunteerService.getAllVolunteers().stream()
            .map(volunteer -> new VolunteerDTO(
                volunteer.getVolunteerId(),
                volunteer.getName(),
                volunteer.getRole(),
                volunteer.getContact(),
                volunteer.getIsAssigned(),
                volunteer.getClub() != null ? volunteer.getClub().getClub_id() : null,
                volunteer.getClub() != null ? volunteer.getClub().getName() : null,
                volunteer.getDepartment() != null ? volunteer.getDepartment().getDepartmentId() : null,
                volunteer.getDepartment() != null ? volunteer.getDepartment().getName() : null,
                volunteer.getVenue() != null ? volunteer.getVenue().getVenue_id() : null,
                volunteer.getVenue() != null ? volunteer.getVenue().getName() : null,
                volunteer.getEvent() != null ? volunteer.getEvent().getEvent_id() : null,
                volunteer.getEvent() != null ? volunteer.getEvent().getEvent_name() : null
            ))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/volunteers/{id}")
    public ResponseEntity<VolunteerDTO> getVolunteerById(@PathVariable int id) {
        Volunteer volunteer = volunteerService.getVolunteerById(id).orElse(null);
        if (volunteer != null) {
            VolunteerDTO dto = new VolunteerDTO(
                volunteer.getVolunteerId(),
                volunteer.getName(),
                volunteer.getRole(),
                volunteer.getContact(),
                volunteer.getIsAssigned(),
                volunteer.getClub() != null ? volunteer.getClub().getClub_id() : null,
                volunteer.getClub() != null ? volunteer.getClub().getName() : null,
                volunteer.getDepartment() != null ? volunteer.getDepartment().getDepartmentId() : null,
                volunteer.getDepartment() != null ? volunteer.getDepartment().getName() : null,
                volunteer.getVenue() != null ? volunteer.getVenue().getVenue_id() : null,
                volunteer.getVenue() != null ? volunteer.getVenue().getName() : null,
                volunteer.getEvent() != null ? volunteer.getEvent().getEvent_id() : null,
                volunteer.getEvent() != null ? volunteer.getEvent().getEvent_name() : null
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/volunteers")
    public Volunteer createVolunteer(@RequestBody Volunteer volunteer) {
        return volunteerService.createVolunteer(volunteer);
    }
    
    @PutMapping("/volunteers/{id}")
    public ResponseEntity<Volunteer> updateVolunteer(@PathVariable int id, @RequestBody Volunteer volunteer) {
        try {
            Volunteer updatedVolunteer = volunteerService.updateVolunteer(id, volunteer);
            return ResponseEntity.ok(updatedVolunteer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/volunteers/{id}")
    public ResponseEntity<?> deleteVolunteer(@PathVariable int id) {
        System.out.println("DELETE request received for volunteer ID: " + id);
        try {
            volunteerService.deleteVolunteer(id);
            System.out.println("Volunteer deleted successfully: " + id);
            return ResponseEntity.ok().body("Volunteer deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting volunteer " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
