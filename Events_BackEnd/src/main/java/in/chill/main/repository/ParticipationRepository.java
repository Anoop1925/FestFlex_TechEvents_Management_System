package in.chill.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    @Query("SELECT p FROM Participation p WHERE p.event.event_id = :eventId")
    List<Participation> findByEventId(@Param("eventId") int eventId);
}
