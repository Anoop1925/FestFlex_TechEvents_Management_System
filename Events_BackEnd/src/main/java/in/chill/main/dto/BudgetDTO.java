package in.chill.main.dto;

public class BudgetDTO {
    private int budgetId;
    private float allocatedAmount;
    private float utilizedAmount;
    private Integer sponsorId;
    private String sponsorName;
    private Integer clubId;
    private String clubName;
    private Integer eventId;
    private String eventName;

    // Constructor with all parameters
    public BudgetDTO(int budgetId, float allocatedAmount, float utilizedAmount, 
                     Integer sponsorId, String sponsorName, 
                     Integer clubId, String clubName, 
                     Integer eventId, String eventName) {
        this.budgetId = budgetId;
        this.allocatedAmount = allocatedAmount;
        this.utilizedAmount = utilizedAmount;
        this.sponsorId = sponsorId;
        this.sponsorName = sponsorName;
        this.clubId = clubId;
        this.clubName = clubName;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    // Getters and setters
    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public float getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(float allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public float getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(float utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public Integer getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
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
