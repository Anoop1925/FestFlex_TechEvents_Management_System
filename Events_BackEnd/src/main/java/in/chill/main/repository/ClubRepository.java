package in.chill.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.chill.main.entity.Club;
 
public interface ClubRepository extends JpaRepository<Club, Integer> {
    
    // Cascade delete queries - delete all related records before deleting club
    
    @Modifying
    @Query(value = "DELETE FROM result WHERE event_id IN (SELECT event_id FROM events WHERE club_id = ?1)", nativeQuery = true)
    void deleteResultsByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM participation WHERE event_id IN (SELECT event_id FROM events WHERE club_id = ?1)", nativeQuery = true)
    void deleteParticipationsByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM budget WHERE club_id = ?1", nativeQuery = true)
    void deleteBudgetsByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM volunteer WHERE club_id = ?1", nativeQuery = true)
    void deleteVolunteersByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM sponsor WHERE club_id = ?1", nativeQuery = true)
    void deleteSponsorsByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM events WHERE club_id = ?1", nativeQuery = true)
    void deleteEventsByClubId(int clubId);
    
    @Modifying
    @Query(value = "DELETE FROM club WHERE club_id = ?1", nativeQuery = true)
    void deleteClubById(int clubId);
} 