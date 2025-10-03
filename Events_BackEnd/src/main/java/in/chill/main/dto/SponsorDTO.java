package in.chill.main.dto;

public class SponsorDTO {
    private int sponsorId;
    private String name;
    private boolean isCash;
    private String mode;
    private String contact;
    private Integer clubId;
    private String clubName;
    
    // Constructor
    public SponsorDTO() {}
    
    public SponsorDTO(int sponsorId, String name, boolean isCash, String mode, String contact, Integer clubId, String clubName) {
        this.sponsorId = sponsorId;
        this.name = name;
        this.isCash = isCash;
        this.mode = mode;
        this.contact = contact;
        this.clubId = clubId;
        this.clubName = clubName;
    }
    
    // Getters and Setters
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
    
    public boolean isIsCash() {
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
}
