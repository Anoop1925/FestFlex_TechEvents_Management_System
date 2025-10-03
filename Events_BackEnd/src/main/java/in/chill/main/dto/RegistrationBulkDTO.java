package in.chill.main.dto;

import java.time.LocalDateTime;

public class RegistrationBulkDTO {
    private String name;
    private String college;
    private String email;
    private String contact;
    private LocalDateTime registeredAt;

    // Default constructor
    public RegistrationBulkDTO() {}

    // Parameterized constructor
    public RegistrationBulkDTO(String name, String college, String email, String contact, LocalDateTime registeredAt) {
        this.name = name;
        this.college = college;
        this.email = email;
        this.contact = contact;
        this.registeredAt = registeredAt;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
