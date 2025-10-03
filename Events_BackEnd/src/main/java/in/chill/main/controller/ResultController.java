package in.chill.main.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import in.chill.main.dto.ResultDTO;
import in.chill.main.entity.Result;
import in.chill.main.services.ResultService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResultController {
    
    @Autowired
    private ResultService resultService;
    
    @GetMapping("/results")
    public List<ResultDTO> getAllResults() {
        return resultService.getAllResults().stream()
            .map(result -> new ResultDTO(
                result.getResultId(),
                result.getRank(),
                result.getScore(),
                result.getEvent() != null ? result.getEvent().getEvent_id() : null,
                result.getEvent() != null ? result.getEvent().getEvent_name() : null,
                result.getParticipation() != null ? result.getParticipation().getParticipationId() : null,
                result.getParticipation() != null && result.getParticipation().getRegistration() != null 
                    ? result.getParticipation().getRegistration().getName() : null,
                result.getParticipation() != null && result.getParticipation().getRegistration() != null 
                    ? result.getParticipation().getRegistration().getCollege() : null
            ))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/results/{id}")
    public ResponseEntity<ResultDTO> getResultById(@PathVariable int id) {
        Result result = resultService.getResultById(id).orElse(null);
        if (result != null) {
            ResultDTO dto = new ResultDTO(
                result.getResultId(),
                result.getRank(),
                result.getScore(),
                result.getEvent() != null ? result.getEvent().getEvent_id() : null,
                result.getEvent() != null ? result.getEvent().getEvent_name() : null,
                result.getParticipation() != null ? result.getParticipation().getParticipationId() : null,
                result.getParticipation() != null && result.getParticipation().getRegistration() != null 
                    ? result.getParticipation().getRegistration().getName() : null,
                result.getParticipation() != null && result.getParticipation().getRegistration() != null 
                    ? result.getParticipation().getRegistration().getCollege() : null
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/results")
    public Result createResult(@RequestBody Result result) {
        return resultService.createResult(result);
    }
    
    @PutMapping("/results/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable int id, @RequestBody Result result) {
        try {
            Result updatedResult = resultService.updateResult(id, result);
            return ResponseEntity.ok(updatedResult);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/results/{id}")
    public ResponseEntity<String> deleteResult(@PathVariable int id) {
        System.out.println("DELETE request received for result ID: " + id);
        try {
            resultService.deleteResult(id);
            System.out.println("Result deleted successfully: " + id);
            return ResponseEntity.ok("Result deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting result " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
