package in.chill.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {
    
    // Cascade delete queries - delete all related records before deleting sponsor
    
    @Modifying
    @Query(value = "DELETE FROM budget WHERE sponsor_id = ?1", nativeQuery = true)
    void deleteBudgetsBySponsorId(int sponsorId);
    
    @Modifying
    @Query(value = "DELETE FROM sponsor WHERE sponsor_id = ?1", nativeQuery = true)
    void deleteSponsorById(int sponsorId);
}
