package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import in.chill.main.entity.Events;

public interface EventsService{
	
	public Events createEvent(Events event);
	public List<Events> getAllEvents();
	public Optional<Events> getEventDetails(int id);
	public Events updateEventDetails(int id, Events event);
	
	// New methods for frontend integration
	public Optional<Events> getNextEvent();
	public List<Events> getUpcomingEvents();
	public int getCompletedEventsCount();
	public Float getEventFee(int eventId);
	
	// Delete method
	public void deleteEvent(int id);

}
