package in.chill.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
    
    // Cascade delete queries - delete all related records before deleting venue
    
    @Modifying
    @Query(value = "DELETE FROM result WHERE event_id IN (SELECT event_id FROM events WHERE venue_id = ?1)", nativeQuery = true)
    void deleteResultsByVenueId(int venueId);
    
    @Modifying
    @Query(value = "DELETE FROM participation WHERE event_id IN (SELECT event_id FROM events WHERE venue_id = ?1)", nativeQuery = true)
    void deleteParticipationsByVenueId(int venueId);
    
    @Modifying
    @Query(value = "DELETE FROM budget WHERE event_id IN (SELECT event_id FROM events WHERE venue_id = ?1)", nativeQuery = true)
    void deleteBudgetsByVenueId(int venueId);
    
    @Modifying
    @Query(value = "DELETE FROM volunteer WHERE venue_id = ?1", nativeQuery = true)
    void deleteVolunteersByVenueId(int venueId);
    
    @Modifying
    @Query(value = "DELETE FROM events WHERE venue_id = ?1", nativeQuery = true)
    void deleteEventsByVenueId(int venueId);
    
    @Modifying
    @Query(value = "DELETE FROM venue WHERE venue_id = ?1", nativeQuery = true)
    void deleteVenueById(int venueId);
} 