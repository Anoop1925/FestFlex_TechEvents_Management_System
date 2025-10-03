package in.chill.main.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.chill.main.entity.Events;
import in.chill.main.entity.Participation;
import in.chill.main.repository.EventsRepository;
import in.chill.main.repository.ParticipationRepository;

@Service
public class EventsServiceImplementation implements EventsService {
	
	@Autowired
	private EventsRepository eventsRepository;
	
	@Autowired
	private ParticipationRepository participationRepository;
	
	@Override
	public Events createEvent(Events event) {
		return eventsRepository.save(event);
	}

	@Override
	public List<Events> getAllEvents() {
		return eventsRepository.findAll();
	}

	@Override
	public Optional<Events> getEventDetails(int id) {
		return eventsRepository.findById(id);
	}

	@Override
	public Events updateEventDetails(int id, Events event) {
		Events existingEvent = eventsRepository.findById(id).orElse(null);
		if(existingEvent != null) {
			// Update all fields
			existingEvent.setEvent_name(event.getEvent_name());
			existingEvent.setEvent_start_date(event.getEvent_start_date());
			existingEvent.setEvent_end_date(event.getEvent_end_date());
			existingEvent.setEvent_time(event.getEvent_time());
			existingEvent.setEvent_type(event.getEvent_type());
			existingEvent.setEvent_description(event.getEvent_description());
			existingEvent.setJudge_id(event.getJudge_id());
			existingEvent.setClub_id(event.getClub_id());
			existingEvent.setVenue_id(event.getVenue_id());
			
			return eventsRepository.save(existingEvent);
		}
		else {
			 throw new RuntimeException("Event does not exist");
		}
	}
	
	@Override
	public Optional<Events> getNextEvent() {
		return eventsRepository.findNextEventNative();
	}
	
	@Override
	public List<Events> getUpcomingEvents() {
		return eventsRepository.findUpcomingEventsNative();
	}
	
	@Override
	public int getCompletedEventsCount() {
		return eventsRepository.countCompletedEvents();
	}
	
	@Override
	public Float getEventFee(int eventId) {
		// First, try to get the most common fee from existing participations for this event
		List<Participation> participations = participationRepository.findByEventId(eventId);
		
		if (!participations.isEmpty()) {
			// Find the most common fee amount (excluding 0.0 fees)
			Map<Float, Integer> feeCount = new HashMap<>();
			for (Participation participation : participations) {
				float fee = participation.getEventAmount();
				// Only count non-zero fees
				if (fee > 0) {
					feeCount.put(fee, feeCount.getOrDefault(fee, 0) + 1);
				}
			}
			
			// Return the most common non-zero fee
			Optional<Float> mostCommonFee = feeCount.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey);
				
			if (mostCommonFee.isPresent()) {
				return mostCommonFee.get();
			}
		}
		
		// If no participations exist, return default fee based on event type
		return getDefaultFeeByEventType(eventId);
	}
	
	private Float getDefaultFeeByEventType(int eventId) {
		Optional<Events> eventOpt = eventsRepository.findById(eventId);
		if (eventOpt.isPresent()) {
			Events event = eventOpt.get();
			String eventType = event.getEvent_type();
			
			// Default fee structure based on event type (matching frontend logic)
			switch (eventType) {
				case "Technical": return 500.0f;
				case "Cultural": return 300.0f;
				case "Sports": return 400.0f;
				case "Workshop": return 250.0f;
				case "Conference": return 750.0f;
				case "Competition": return 600.0f;
				default: return 350.0f;
			}
		}
		return 350.0f; // Default fallback fee
	}
	
	@Override
	@Transactional
	public void deleteEvent(int id) {
		// Check if event exists
		if (!eventsRepository.existsById(id)) {
			throw new RuntimeException("Event not found with id: " + id);
		}
		
		// Delete all related records first to avoid foreign key constraint violations
		// Order matters: delete child records before parent
		eventsRepository.deleteResultsByEventId(id);        // Delete results first
		eventsRepository.deleteParticipationsByEventId(id); // Delete participations
		eventsRepository.deleteBudgetsByEventId(id);        // Delete budgets
		eventsRepository.deleteVolunteersByEventId(id);     // Delete volunteers
		
		// Finally delete the event itself
		eventsRepository.deleteEventById(id);
	}

}
