package in.chill.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.chill.main.dto.DashboardCountsDTO;
import in.chill.main.dto.MonthlyRegistrationDTO;
import in.chill.main.repository.AdminRepository;
import in.chill.main.repository.BudgetRepository;
import in.chill.main.repository.ClubRepository;
import in.chill.main.repository.CommentRepository;
import in.chill.main.repository.DepartmentRepository;
import in.chill.main.repository.EventsRepository;
import in.chill.main.repository.FeedbackRepository;
import in.chill.main.repository.JudgeRepository;
import in.chill.main.repository.ParticipationRepository;
import in.chill.main.repository.RegistrationRepository;
import in.chill.main.repository.ResultRepository;
import in.chill.main.repository.SponsorRepository;
import in.chill.main.repository.UserRepository;
import in.chill.main.repository.VenueRepository;
import in.chill.main.repository.VolunteerRepository;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Get all entity counts for dashboard
     */
    @GetMapping("/counts")
    public ResponseEntity<DashboardCountsDTO> getDashboardCounts() {
        DashboardCountsDTO counts = new DashboardCountsDTO();
        
        counts.setEvents(eventsRepository.count());
        counts.setClubs(clubRepository.count());
        counts.setParticipants(participationRepository.count());
        counts.setRegistrations(registrationRepository.count());
        counts.setSponsors(sponsorRepository.count());
        counts.setVenues(venueRepository.count());
        counts.setVolunteers(volunteerRepository.count());
        counts.setBudgets(budgetRepository.count());
        counts.setDepartments(departmentRepository.count());
        counts.setResults(resultRepository.count());
        counts.setJudges(judgeRepository.count());
        counts.setFeedback(feedbackRepository.count());
        counts.setAdmins(adminRepository.count());
        counts.setUsers(userRepository.count());
        counts.setComments(commentRepository.count());

        return ResponseEntity.ok(counts);
    }

    /**
     * Get monthly registration counts based on registeredAt timestamp
     * Returns data for last 5 months plus current month (6 months total)
     */
    @GetMapping("/registrations/monthly")
    public ResponseEntity<List<MonthlyRegistrationDTO>> getMonthlyRegistrations() {
        // Get all registrations
        var registrations = registrationRepository.findAll();
        
        // Group by month
        Map<String, Long> monthlyData = new TreeMap<>();
        
        for (var registration : registrations) {
            if (registration.getRegisteredAt() != null) {
                String monthKey = registration.getRegisteredAt().getYear() + "-" + 
                                String.format("%02d", registration.getRegisteredAt().getMonthValue());
                monthlyData.put(monthKey, monthlyData.getOrDefault(monthKey, 0L) + 1);
            }
        }
        
        // Convert to DTOs
        List<MonthlyRegistrationDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : monthlyData.entrySet()) {
            result.add(new MonthlyRegistrationDTO(entry.getKey(), entry.getValue()));
        }
        
        return ResponseEntity.ok(result);
    }
}
