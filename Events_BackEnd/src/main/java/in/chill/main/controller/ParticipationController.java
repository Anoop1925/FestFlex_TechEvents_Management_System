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

import in.chill.main.dto.ParticipationDTO;
import in.chill.main.entity.Participation;
import in.chill.main.services.ParticipationService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ParticipationController {
    
    @Autowired
    private ParticipationService participationService;
    
    @GetMapping("/participations")
    public List<ParticipationDTO> getAllParticipations() {
        return participationService.getAllParticipations().stream()
            .map(p -> new ParticipationDTO(
                p.getParticipationId(),
                p.getEventAmount(),
                p.getRegistration() != null ? p.getRegistration().getRegistrationId() : null,
                p.getRegistration() != null ? p.getRegistration().getName() : null,
                p.getRegistration() != null ? p.getRegistration().getCollege() : null,
                p.getEvent() != null ? p.getEvent().getEvent_id() : null,
                p.getEvent() != null ? p.getEvent().getEvent_name() : null
            ))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/participations/{id}")
    public ResponseEntity<ParticipationDTO> getParticipationById(@PathVariable int id) {
        Participation p = participationService.getParticipationById(id).orElse(null);
        if (p != null) {
            ParticipationDTO dto = new ParticipationDTO(
                p.getParticipationId(),
                p.getEventAmount(),
                p.getRegistration() != null ? p.getRegistration().getRegistrationId() : null,
                p.getRegistration() != null ? p.getRegistration().getName() : null,
                p.getRegistration() != null ? p.getRegistration().getCollege() : null,
                p.getEvent() != null ? p.getEvent().getEvent_id() : null,
                p.getEvent() != null ? p.getEvent().getEvent_name() : null
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/participations")
    public Participation createParticipation(@RequestBody Participation participation) {
        return participationService.createParticipation(participation);
    }
    
    @PutMapping("/participations/{id}")
    public ResponseEntity<Participation> updateParticipation(@PathVariable int id, @RequestBody Participation participation) {
        try {
            Participation updatedParticipation = participationService.updateParticipation(id, participation);
            return ResponseEntity.ok(updatedParticipation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/participations/{id}")
    public ResponseEntity<String> deleteParticipation(@PathVariable int id) {
        System.out.println("DELETE request received for participation ID: " + id);
        try {
            participationService.deleteParticipation(id);
            System.out.println("Participation deleted successfully: " + id);
            return ResponseEntity.ok("Participation deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting participation " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
