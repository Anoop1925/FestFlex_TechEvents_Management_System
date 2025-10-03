package in.chill.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "participation")
public class Participation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private int participationId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private Registration registration;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Events event;
    
    @Column(name = "event_amount")
    private float eventAmount;
    
    // Default constructor
    public Participation() {}
    
    // Constructor with parameters
    public Participation(Registration registration, Events event, float eventAmount) {
        this.registration = registration;
        this.event = event;
        this.eventAmount = eventAmount;
    }
    
    // Getters and setters
    public int getParticipationId() {
        return participationId;
    }
    
    public void setParticipationId(int participationId) {
        this.participationId = participationId;
    }
    
    public Registration getRegistration() {
        return registration;
    }
    
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
    
    public Events getEvent() {
        return event;
    }
    
    public void setEvent(Events event) {
        this.event = event;
    }
    
    public float getEventAmount() {
        return eventAmount;
    }
    
    public void setEventAmount(float eventAmount) {
        this.eventAmount = eventAmount;
    }
    
    @Override
    public String toString() {
        return "Participation{" +
                "participationId=" + participationId +
                ", registration=" + (registration != null ? registration.getRegistrationId() : null) +
                ", event=" + (event != null ? event.getEvent_id() : null) +
                ", eventAmount=" + eventAmount +
                '}';
    }
}
