package in.chill.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Events;

@Repository
public interface EventsRepository extends JpaRepository<Events, Integer>{
	
	// Find the next upcoming event (closest start date in the future)
	// Since dates are stored as strings in YYYY-MM-DD format, we can use string comparison
	@Query("SELECT e FROM Events e WHERE e.event_start_date >= FUNCTION('CURDATE') ORDER BY e.event_start_date ASC, e.event_time ASC")
	Optional<Events> findNextEvent();
	
	// Find all upcoming events (start date is today or in the future)
	@Query("SELECT e FROM Events e WHERE e.event_start_date >= FUNCTION('CURDATE') ORDER BY e.event_start_date ASC, e.event_time ASC")
	List<Events> findUpcomingEvents();
	
	// Find events by date range
	@Query("SELECT e FROM Events e WHERE e.event_start_date BETWEEN ?1 AND ?2 ORDER BY e.event_start_date ASC")
	List<Events> findEventsByDateRange(String startDate, String endDate);
	
	// Alternative queries using string comparison (more reliable for our string date format)
	// Note: event_time contains format like "09:00 AM - 10:00 AM", so we compare only dates
	// Cast VARCHAR date to DATE for comparison
	@Query(value = "SELECT * FROM events WHERE event_start_date::DATE >= CURRENT_DATE ORDER BY event_start_date ASC, event_time ASC LIMIT 1", nativeQuery = true)
	Optional<Events> findNextEventNative();
	
	@Query(value = "SELECT * FROM events WHERE event_start_date::DATE >= CURRENT_DATE ORDER BY event_start_date ASC, event_time ASC", nativeQuery = true)
	List<Events> findUpcomingEventsNative();
	
	// Count completed events (events where end date is before current date)
	@Query(value = "SELECT COUNT(*) FROM events WHERE event_end_date::DATE < CURRENT_DATE", nativeQuery = true)
	int countCompletedEvents();
	
	// Delete event with all related records - proper cascade delete
	@Modifying
	@Query(value = "DELETE FROM events WHERE event_id = ?1", nativeQuery = true)
	void deleteEventById(int eventId);
	
	// Delete related budget records
	@Modifying
	@Query(value = "DELETE FROM budget WHERE event_id = ?1", nativeQuery = true)
	void deleteBudgetsByEventId(int eventId);
	
	// Delete related participation records
	@Modifying
	@Query(value = "DELETE FROM participation WHERE event_id = ?1", nativeQuery = true)
	void deleteParticipationsByEventId(int eventId);
	
	// Delete related result records  
	@Modifying
	@Query(value = "DELETE FROM result WHERE event_id = ?1", nativeQuery = true)
	void deleteResultsByEventId(int eventId);
	
	// Delete related volunteer records
	@Modifying
	@Query(value = "DELETE FROM volunteer WHERE event_id = ?1", nativeQuery = true)
	void deleteVolunteersByEventId(int eventId);

}
