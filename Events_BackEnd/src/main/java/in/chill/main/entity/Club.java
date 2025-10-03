package in.chill.main.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "club")
public class Club {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private int club_id;
    
    @Column(name = "club_name")
    private String name;
    
    @Column(name = "president_name")
    private String presidentName;
    
    @Column(name = "president_contact")
    private String presidentContact;
    
    @Column(name = "president_email")
    private String presidentEmail;
    
    // Relationships
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Events> events;
    
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Volunteer> volunteers;
    
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sponsor> sponsors;
    
    // Default constructor
    public Club() {}
    
    // Constructor with parameters
    public Club(String name, String presidentName, String presidentContact, String presidentEmail) {
        this.name = name;
        this.presidentName = presidentName;
        this.presidentContact = presidentContact;
        this.presidentEmail = presidentEmail;
    }
    
    // Getters and setters
    public int getClub_id() {
        return club_id;
    }
    
    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPresidentName() {
        return presidentName;
    }
    
    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }
    
    public String getPresidentContact() {
        return presidentContact;
    }
    
    public void setPresidentContact(String presidentContact) {
        this.presidentContact = presidentContact;
    }
    
    public String getPresidentEmail() {
        return presidentEmail;
    }
    
    public void setPresidentEmail(String presidentEmail) {
        this.presidentEmail = presidentEmail;
    }
    
    public List<Events> getEvents() {
        return events;
    }
    
    public void setEvents(List<Events> events) {
        this.events = events;
    }
    
    public List<Volunteer> getVolunteers() {
        return volunteers;
    }
    
    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }
    
    public List<Sponsor> getSponsors() {
        return sponsors;
    }
    
    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }
    
    @Override
    public String toString() {
        return "Club{" +
                "club_id=" + club_id +
                ", name='" + name + '\'' +
                ", presidentName='" + presidentName + '\'' +
                ", presidentContact='" + presidentContact + '\'' +
                ", presidentEmail='" + presidentEmail + '\'' +
                '}';
    }
} 