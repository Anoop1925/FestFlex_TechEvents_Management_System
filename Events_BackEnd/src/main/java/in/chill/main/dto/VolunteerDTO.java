package in.chill.main.dto;

public class VolunteerDTO {
    private int volunteerId;
    private String name;
    private String role;
    private String contact;
    private boolean isAssigned;
    private Integer clubId;
    private String clubName;
    private Integer departmentId;
    private String departmentName;
    private Integer venueId;
    private String venueName;
    private Integer eventId;
    private String eventName;
    
    // Constructors
    public VolunteerDTO() {}
    
    public VolunteerDTO(int volunteerId, String name, String role, String contact, boolean isAssigned,
                       Integer clubId, String clubName, Integer departmentId, String departmentName,
                       Integer venueId, String venueName, Integer eventId, String eventName) {
        this.volunteerId = volunteerId;
        this.name = name;
        this.role = role;
        this.contact = contact;
        this.isAssigned = isAssigned;
        this.clubId = clubId;
        this.clubName = clubName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.venueId = venueId;
        this.venueName = venueName;
        this.eventId = eventId;
        this.eventName = eventName;
    }
    
    // Getters and Setters
    public int getVolunteerId() {
        return volunteerId;
    }
    
    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public boolean isAssigned() {
        return isAssigned;
    }
    
    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
    
    public Integer getClubId() {
        return clubId;
    }
    
    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }
    
    public String getClubName() {
        return clubName;
    }
    
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    
    public Integer getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public Integer getVenueId() {
        return venueId;
    }
    
    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
    
    public String getVenueName() {
        return venueName;
    }
    
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    
    public Integer getEventId() {
        return eventId;
    }
    
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
