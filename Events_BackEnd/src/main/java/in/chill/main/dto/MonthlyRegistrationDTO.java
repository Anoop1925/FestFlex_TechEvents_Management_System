package in.chill.main.dto;

public class MonthlyRegistrationDTO {
    private String month; // e.g., "2025-01" for January 2025
    private long count;

    // Constructors
    public MonthlyRegistrationDTO() {}

    public MonthlyRegistrationDTO(String month, long count) {
        this.month = month;
        this.count = count;
    }

    // Getters and Setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
