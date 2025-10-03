package in.chill.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.chill.main.dto.EventBulkDTO;
import in.chill.main.dto.ParticipationBulkDTO;
import in.chill.main.dto.RegistrationBulkDTO;
import in.chill.main.entity.Events;
import in.chill.main.entity.Participation;
import in.chill.main.entity.Registration;
import in.chill.main.repository.EventsRepository;
import in.chill.main.repository.ParticipationRepository;
import in.chill.main.repository.RegistrationRepository;

@RestController
@RequestMapping("/api/admin/bulk")
@CrossOrigin(origins = "http://localhost:4200")
public class BulkDataController {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    /**
     * Bulk insert events
     */
    @PostMapping("/events")
    public ResponseEntity<String> bulkInsertEvents(@RequestBody List<EventBulkDTO> eventDTOs) {
        try {
            List<Events> events = new ArrayList<>();
            
            for (EventBulkDTO dto : eventDTOs) {
                Events event = new Events();
                event.setEvent_name(dto.getEventName());
                event.setEvent_start_date(dto.getEventStartDate());
                event.setEvent_end_date(dto.getEventEndDate());
                event.setEvent_time(dto.getEventTime());
                event.setEvent_type(dto.getEventType());
                event.setEvent_description(dto.getEventDescription());
                event.setJudge_id(dto.getJudgeId());
                event.setClub_id(dto.getClubId());
                event.setVenue_id(dto.getVenueId());
                
                events.add(event);
            }
            
            eventsRepository.saveAll(events);
            return ResponseEntity.ok("Successfully inserted " + events.size() + " events");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error inserting events: " + e.getMessage());
        }
    }

    /**
     * Bulk insert registrations with timestamps
     */
    @PostMapping("/registrations")
    public ResponseEntity<String> bulkInsertRegistrations(@RequestBody List<RegistrationBulkDTO> registrationDTOs) {
        try {
            List<Registration> registrations = new ArrayList<>();
            
            for (RegistrationBulkDTO dto : registrationDTOs) {
                Registration registration = new Registration();
                registration.setName(dto.getName());
                registration.setCollege(dto.getCollege());
                registration.setEmail(dto.getEmail());
                registration.setContact(dto.getContact());
                registration.setRegisteredAt(dto.getRegisteredAt());
                
                registrations.add(registration);
            }
            
            registrationRepository.saveAll(registrations);
            return ResponseEntity.ok("Successfully inserted " + registrations.size() + " registrations");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error inserting registrations: " + e.getMessage());
        }
    }

    /**
     * Bulk insert participations
     */
    @PostMapping("/participations")
    public ResponseEntity<String> bulkInsertParticipations(@RequestBody List<ParticipationBulkDTO> participationDTOs) {
        try {
            List<Participation> participations = new ArrayList<>();
            
            for (ParticipationBulkDTO dto : participationDTOs) {
                Registration registration = registrationRepository.findById(dto.getRegistrationId())
                        .orElseThrow(() -> new RuntimeException("Registration not found: " + dto.getRegistrationId()));
                
                Events event = eventsRepository.findById(dto.getEventId())
                        .orElseThrow(() -> new RuntimeException("Event not found: " + dto.getEventId()));
                
                Participation participation = new Participation();
                participation.setRegistration(registration);
                participation.setEvent(event);
                participation.setEventAmount(dto.getEventAmount());
                
                participations.add(participation);
            }
            
            participationRepository.saveAll(participations);
            return ResponseEntity.ok("Successfully inserted " + participations.size() + " participations");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error inserting participations: " + e.getMessage());
        }
    }
}
