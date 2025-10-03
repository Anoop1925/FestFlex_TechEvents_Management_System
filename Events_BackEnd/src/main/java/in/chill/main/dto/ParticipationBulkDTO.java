package in.chill.main.dto;

public class ParticipationBulkDTO {
    private Integer registrationId;
    private Integer eventId;
    private Float eventAmount;

    // Default constructor
    public ParticipationBulkDTO() {}

    // Parameterized constructor
    public ParticipationBulkDTO(Integer registrationId, Integer eventId, Float eventAmount) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.eventAmount = eventAmount;
    }

    // Getters and Setters
    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Float getEventAmount() {
        return eventAmount;
    }

    public void setEventAmount(Float eventAmount) {
        this.eventAmount = eventAmount;
    }
}
