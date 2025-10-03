package in.chill.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sponsor")
public class Sponsor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sponsor_id")
    private int sponsorId;
    
    @Column(name = "sponsor_name")
    private String name;
    
    @Column(name = "is_cash")
    private boolean isCash;
    
    @Column(name = "sponsor_mode")
    private String mode;
    
    @Column(name = "sponsor_contact")
    private String contact;
    
    @ManyToOne
    @JoinColumn(name = "club_id")
    @JsonIgnoreProperties({"sponsors", "events", "volunteers", "budget"})
    private Club club;
    
    // Default constructor
    public Sponsor() {}
    
    // Constructor with parameters
    public Sponsor(String name, boolean isCash, String mode, String contact, Club club) {
        this.name = name;
        this.isCash = isCash;
        this.mode = mode;
        this.contact = contact;
        this.club = club;
    }
    
    // Getters and setters
    public int getSponsorId() {
        return sponsorId;
    }
    
    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean getIsCash() {
        return isCash;
    }
    
    public void setIsCash(boolean isCash) {
        this.isCash = isCash;
    }
    
    public String getMode() {
        return mode;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public Club getClub() {
        return club;
    }
    
    public void setClub(Club club) {
        this.club = club;
    }
    
    @Override
    public String toString() {
        return "Sponsor{" +
                "sponsorId=" + sponsorId +
                ", name='" + name + '\'' +
                ", isCash=" + isCash +
                ", mode='" + mode + '\'' +
                ", contact='" + contact + '\'' +
                ", club=" + (club != null ? club.getClub_id() : null) +
                '}';
    }
}
