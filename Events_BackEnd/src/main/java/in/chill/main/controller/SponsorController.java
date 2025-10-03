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

import in.chill.main.dto.SponsorDTO;
import in.chill.main.entity.Sponsor;
import in.chill.main.services.SponsorService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SponsorController {
    
    @Autowired
    private SponsorService sponsorService;
    
    @GetMapping("/sponsors")
    public List<SponsorDTO> getAllSponsors() {
        return sponsorService.getAllSponsors().stream()
            .map(sponsor -> new SponsorDTO(
                sponsor.getSponsorId(),
                sponsor.getName(),
                sponsor.getIsCash(),
                sponsor.getMode(),
                sponsor.getContact(),
                sponsor.getClub() != null ? sponsor.getClub().getClub_id() : null,
                sponsor.getClub() != null ? sponsor.getClub().getName() : null
            ))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/sponsors/{id}")
    public ResponseEntity<SponsorDTO> getSponsorById(@PathVariable int id) {
        Sponsor sponsor = sponsorService.getSponsorById(id).orElse(null);
        if (sponsor != null) {
            SponsorDTO dto = new SponsorDTO(
                sponsor.getSponsorId(),
                sponsor.getName(),
                sponsor.getIsCash(),
                sponsor.getMode(),
                sponsor.getContact(),
                sponsor.getClub() != null ? sponsor.getClub().getClub_id() : null,
                sponsor.getClub() != null ? sponsor.getClub().getName() : null
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/sponsors")
    public Sponsor createSponsor(@RequestBody Sponsor sponsor) {
        return sponsorService.createSponsor(sponsor);
    }
    
    @PutMapping("/sponsors/{id}")
    public ResponseEntity<Sponsor> updateSponsor(@PathVariable int id, @RequestBody Sponsor sponsor) {
        try {
            Sponsor updatedSponsor = sponsorService.updateSponsor(id, sponsor);
            return ResponseEntity.ok(updatedSponsor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/sponsors/{id}")
    public ResponseEntity<?> deleteSponsor(@PathVariable int id) {
        System.out.println("DELETE request received for sponsor ID: " + id);
        try {
            sponsorService.deleteSponsor(id);
            System.out.println("Sponsor deleted successfully: " + id);
            return ResponseEntity.ok().body("Sponsor deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting sponsor " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
