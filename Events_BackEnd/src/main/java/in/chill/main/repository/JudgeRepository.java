package in.chill.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Judge;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, Integer> {
    
    // Cascade delete queries - delete all related records before deleting judge
    
    @Modifying
    @Query(value = "DELETE FROM result WHERE event_id IN (SELECT event_id FROM events WHERE judge_id = ?1)", nativeQuery = true)
    void deleteResultsByJudgeId(int judgeId);
    
    @Modifying
    @Query(value = "DELETE FROM participation WHERE event_id IN (SELECT event_id FROM events WHERE judge_id = ?1)", nativeQuery = true)
    void deleteParticipationsByJudgeId(int judgeId);
    
    @Modifying
    @Query(value = "DELETE FROM budget WHERE event_id IN (SELECT event_id FROM events WHERE judge_id = ?1)", nativeQuery = true)
    void deleteBudgetsByJudgeId(int judgeId);
    
    @Modifying
    @Query(value = "DELETE FROM volunteer WHERE event_id IN (SELECT event_id FROM events WHERE judge_id = ?1)", nativeQuery = true)
    void deleteVolunteersByJudgeId(int judgeId);
    
    @Modifying
    @Query(value = "DELETE FROM events WHERE judge_id = ?1", nativeQuery = true)
    void deleteEventsByJudgeId(int judgeId);
    
    @Modifying
    @Query(value = "DELETE FROM judge WHERE judge_id = ?1", nativeQuery = true)
    void deleteJudgeById(int judgeId);
} 