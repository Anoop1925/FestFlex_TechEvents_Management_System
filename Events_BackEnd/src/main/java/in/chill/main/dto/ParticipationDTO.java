package in.chill.main.dto;

public class ParticipationDTO {
    private int participationId;
    private float eventAmount;
    private Integer registrationId;
    private String participantName;
    private String participantCollege;
    private Integer eventId;
    private String eventName;
    
    // Constructor
    public ParticipationDTO(int participationId, float eventAmount, Integer registrationId, 
                           String participantName, String participantCollege, Integer eventId, String eventName) {
        this.participationId = participationId;
        this.eventAmount = eventAmount;
        this.registrationId = registrationId;
        this.participantName = participantName;
        this.participantCollege = participantCollege;
        this.eventId = eventId;
        this.eventName = eventName;
    }
    
    // Getters and Setters
    public int getParticipationId() {
        return participationId;
    }
    
    public void setParticipationId(int participationId) {
        this.participationId = participationId;
    }
    
    public float getEventAmount() {
        return eventAmount;
    }
    
    public void setEventAmount(float eventAmount) {
        this.eventAmount = eventAmount;
    }
    
    public Integer getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }
    
    public String getParticipantName() {
        return participantName;
    }
    
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    
    public String getParticipantCollege() {
        return participantCollege;
    }
    
    public void setParticipantCollege(String participantCollege) {
        this.participantCollege = participantCollege;
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
