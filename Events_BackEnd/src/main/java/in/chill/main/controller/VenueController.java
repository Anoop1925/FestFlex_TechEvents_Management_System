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

import in.chill.main.entity.Venue;
import in.chill.main.services.VenueService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VenueController {
    
    @Autowired
    private VenueService venueService;
    
    @GetMapping("/venues")
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }
    
    @GetMapping("/venues/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable int id) {
        Venue venue = venueService.getVenueById(id).orElse(null);
        if (venue != null) {
            return ResponseEntity.ok(venue);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/venues")
    public Venue createVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }
    
    @PutMapping("/venues/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable int id, @RequestBody Venue venue) {
        try {
            Venue updatedVenue = venueService.updateVenue(id, venue);
            return ResponseEntity.ok(updatedVenue);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/venues/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable int id) {
        System.out.println("DELETE request received for venue ID: " + id);
        try {
            venueService.deleteVenue(id);
            System.out.println("Venue deleted successfully: " + id);
            return ResponseEntity.ok().body("Venue deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting venue " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
} 