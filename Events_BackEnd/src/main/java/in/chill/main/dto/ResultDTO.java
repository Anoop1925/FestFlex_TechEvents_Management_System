package in.chill.main.dto;

public class ResultDTO {
    private int resultId;
    private int rank;
    private float score;
    private Integer eventId;
    private String eventName;
    private Integer participationId;
    private String participantName;
    private String participantCollege;
    
    // Constructor
    public ResultDTO(int resultId, int rank, float score, Integer eventId, String eventName, 
                     Integer participationId, String participantName, String participantCollege) {
        this.resultId = resultId;
        this.rank = rank;
        this.score = score;
        this.eventId = eventId;
        this.eventName = eventName;
        this.participationId = participationId;
        this.participantName = participantName;
        this.participantCollege = participantCollege;
    }
    
    // Getters and Setters
    public int getResultId() {
        return resultId;
    }
    
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
    
    public int getRank() {
        return rank;
    }
    
    public void setRank(int rank) {
        this.rank = rank;
    }
    
    public float getScore() {
        return score;
    }
    
    public void setScore(float score) {
        this.score = score;
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
    
    public Integer getParticipationId() {
        return participationId;
    }
    
    public void setParticipationId(Integer participationId) {
        this.participationId = participationId;
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
}
