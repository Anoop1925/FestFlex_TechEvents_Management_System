package in.chill.main.dto;

public class EventBulkDTO {
    private String eventName;
    private String eventStartDate;
    private String eventEndDate;
    private String eventTime;
    private String eventType;
    private String eventDescription;
    private Integer judgeId;
    private Integer clubId;
    private Integer venueId;

    // Default constructor
    public EventBulkDTO() {}

    // Parameterized constructor
    public EventBulkDTO(String eventName, String eventStartDate, String eventEndDate, String eventTime,
                        String eventType, String eventDescription, Integer judgeId, Integer clubId, Integer venueId) {
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.judgeId = judgeId;
        this.clubId = clubId;
        this.venueId = venueId;
    }

    // Getters and Setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Integer getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Integer judgeId) {
        this.judgeId = judgeId;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
}
