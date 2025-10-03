package in.chill.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.chill.main.entity.Comment;
import in.chill.main.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import in.chill.main.services.CommentService;
import in.chill.main.services.UserService;
import in.chill.main.util.JwtUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:3000"})
public class CommentController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // Get all comments (public endpoint)
    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }
    
    // Get all comments as DTO for admin (returns flattened structure)
    @GetMapping("/admin/comments")
    public List<in.chill.main.dto.CommentDTO> getAllCommentsForAdmin() {
        System.out.println("GET request received for all comments (admin)");
        return commentService.getAllCommentsDTO();
    }
    
    // Delete comment by ID (admin endpoint - no auth check)
    @DeleteMapping("/admin/comments/{id}")
    public ResponseEntity<String> deleteCommentByAdmin(@PathVariable Long id) {
        System.out.println("DELETE request received for comment ID: " + id);
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get recent comments with limit (public endpoint)
    @GetMapping("/comments/recent")
    public List<Comment> getRecentComments(@RequestParam(defaultValue = "3") int limit) {
        return commentService.getRecentComments(limit);
    }
    
    // Get comment by ID (public endpoint)
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(comment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Create new comment (requires authentication)
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(
            @RequestBody CommentRequest commentRequest,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Validate authentication
            String token = extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token) || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            // Get user from token
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            Optional<User> userOptional = userService.findById(userId);
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(401).body("User not found");
            }
            
            User user = userOptional.get();
            
            // Validate comment content
            if (commentRequest.getContent() == null || commentRequest.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment content cannot be empty");
            }
            
            if (commentRequest.getContent().length() > 1000) {
                return ResponseEntity.badRequest().body("Comment content cannot exceed 1000 characters");
            }
            
            // Create and save comment
            Comment comment = new Comment(commentRequest.getContent().trim(), user);
            Comment savedComment = commentService.createComment(comment);
            
            return ResponseEntity.ok(savedComment);
            
        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    
    // Delete comment (requires authentication - only comment owner or admin)
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Validate authentication
            String token = extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token) || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            // Get user from token
            Long userId = jwtUtil.getUserIdFromToken(token);
            String userEmail = jwtUtil.getEmailFromToken(token);
            
            if (userId == null || userEmail == null) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            // Check if comment exists
            Optional<Comment> commentOptional = commentService.getCommentById(id);
            if (!commentOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Comment comment = commentOptional.get();
            
            // Check if user is the owner of the comment or is admin
            boolean isOwner = comment.getUser().getUserId().equals(userId);
            boolean isAdmin = userEmail.endsWith("@admin.org");
            
            if (!isOwner && !isAdmin) {
                return ResponseEntity.status(403).body("You can only delete your own comments");
            }
            
            // Delete comment
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error deleting comment: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    
    // Get comments by user ID (requires authentication - only for own comments or admin)
    @GetMapping("/comments/user/{userId}")
    public ResponseEntity<?> getCommentsByUser(
            @PathVariable Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Validate authentication
            String token = extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token) || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            // Get user from token
            Long requestingUserId = jwtUtil.getUserIdFromToken(token);
            String userEmail = jwtUtil.getEmailFromToken(token);
            
            if (requestingUserId == null || userEmail == null) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            // Check if user is requesting their own comments or is admin
            boolean isSelfRequest = requestingUserId.equals(userId);
            boolean isAdmin = userEmail.endsWith("@admin.org");
            
            if (!isSelfRequest && !isAdmin) {
                return ResponseEntity.status(403).body("You can only view your own comments");
            }
            
            List<Comment> comments = commentService.getCommentsByUserId(userId);
            return ResponseEntity.ok(comments);
            
        } catch (Exception e) {
            logger.error("Error fetching user comments: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    
    // Get comment count (public endpoint)
    @GetMapping("/comments/count")
    public ResponseEntity<Long> getCommentsCount() {
        Long count = commentService.getCommentsCount();
        return ResponseEntity.ok(count);
    }
    
    // Helper method to extract token from Authorization header
    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    // Inner class for comment request body
    public static class CommentRequest {
        private String content;
        
        public CommentRequest() {}
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}