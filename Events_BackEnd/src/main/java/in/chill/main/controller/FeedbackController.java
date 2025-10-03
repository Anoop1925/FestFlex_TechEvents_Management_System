package in.chill.main.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.chill.main.entity.Feedback;
import in.chill.main.services.FeedbackService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @GetMapping("/feedbacks")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
    
    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
        Feedback feedback = feedbackService.getFeedbackById(id).orElse(null);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/feedbacks")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        try {
            // Set current datetime when creating feedback
            feedback.setDateTime(LocalDateTime.now());
            System.out.println("Setting datetime to: " + LocalDateTime.now());
            System.out.println("Received feedback data: " + feedback.toString());
            
            Feedback savedFeedback = feedbackService.createFeedback(feedback);
            System.out.println("Saved feedback datetime: " + savedFeedback.getDateTime());
            System.out.println("Returning saved feedback: " + savedFeedback.toString());
            
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            System.err.println("Error creating feedback: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @PutMapping("/feedbacks/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable int id, @RequestBody Feedback feedback) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable int id) {
        try {
            System.out.println("DELETE request received for feedback ID: " + id);
            feedbackService.deleteFeedback(id);
            System.out.println("Feedback deleted successfully: " + id);
            return ResponseEntity.ok("Feedback deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting feedback: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
