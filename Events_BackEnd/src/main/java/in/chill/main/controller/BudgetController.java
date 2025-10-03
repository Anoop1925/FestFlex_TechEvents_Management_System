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

import in.chill.main.dto.BudgetDTO;
import in.chill.main.entity.Budget;
import in.chill.main.services.BudgetService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;
    
    @GetMapping("/budgets")
    public List<BudgetDTO> getAllBudgets() {
        return budgetService.getAllBudgets().stream()
            .map(budget -> new BudgetDTO(
                budget.getBudgetId(),
                budget.getAllocatedAmount(),
                budget.getUtilizedAmount(),
                budget.getSponsor() != null ? budget.getSponsor().getSponsorId() : null,
                budget.getSponsor() != null ? budget.getSponsor().getName() : null,
                budget.getClub() != null ? budget.getClub().getClub_id() : null,
                budget.getClub() != null ? budget.getClub().getName() : null,
                budget.getEvent() != null ? budget.getEvent().getEvent_id() : null,
                budget.getEvent() != null ? budget.getEvent().getEvent_name() : null
            ))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/budgets/{id}")
    public ResponseEntity<BudgetDTO> getBudgetById(@PathVariable int id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget != null) {
            BudgetDTO dto = new BudgetDTO(
                budget.getBudgetId(),
                budget.getAllocatedAmount(),
                budget.getUtilizedAmount(),
                budget.getSponsor() != null ? budget.getSponsor().getSponsorId() : null,
                budget.getSponsor() != null ? budget.getSponsor().getName() : null,
                budget.getClub() != null ? budget.getClub().getClub_id() : null,
                budget.getClub() != null ? budget.getClub().getName() : null,
                budget.getEvent() != null ? budget.getEvent().getEvent_id() : null,
                budget.getEvent() != null ? budget.getEvent().getEvent_name() : null
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/budgets")
    public Budget createBudget(@RequestBody Budget budget) {
        return budgetService.createBudget(budget);
    }
    
    @PutMapping("/budgets/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable int id, @RequestBody Budget budget) {
        try {
            Budget updatedBudget = budgetService.updateBudget(id, budget);
            return ResponseEntity.ok(updatedBudget);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/budgets/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable int id) {
        try {
            budgetService.deleteBudget(id);
            System.out.println("Budget with ID " + id + " deleted successfully");
            return ResponseEntity.ok("Budget deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
