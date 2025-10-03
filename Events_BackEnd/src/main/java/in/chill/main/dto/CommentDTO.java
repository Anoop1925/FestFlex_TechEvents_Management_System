package in.chill.main.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    
    // User fields (flattened)
    private Long userId;
    private String userName;
    private String userEmail;
    
    // Default constructor
    public CommentDTO() {}
    
    // Constructor
    public CommentDTO(Long commentId, String content, LocalDateTime createdAt, 
                      Long userId, String userName, String userEmail) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }
    
    // Getters and setters
    public Long getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
