package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.chill.main.entity.Venue;
import in.chill.main.repository.VenueRepository;

@Service
public class VenueServiceImplementation implements VenueService {

    @Autowired
    private VenueRepository venueRepository;
    
    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> getVenueById(int id) {
        return venueRepository.findById(id);
    }

    @Override
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public Venue updateVenue(int id, Venue venue) {
        Venue existingVenue = venueRepository.findById(id).orElse(null);
        if (existingVenue != null) {
            existingVenue.setName(venue.getName());
            existingVenue.setFloor(venue.getFloor());
            return venueRepository.save(existingVenue);
        } else {
            throw new RuntimeException("Venue not found with id: " + id);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteVenue(int id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue not found with id: " + id);
        }
        // Delete in order: child records before parent
        // First delete records from tables that reference events at this venue
        venueRepository.deleteResultsByVenueId(id);
        venueRepository.deleteParticipationsByVenueId(id);
        venueRepository.deleteBudgetsByVenueId(id);
        // Then delete records directly referencing this venue
        venueRepository.deleteVolunteersByVenueId(id);
        venueRepository.deleteEventsByVenueId(id);
        // Finally delete the venue itself
        venueRepository.deleteVenueById(id);
    }
} 