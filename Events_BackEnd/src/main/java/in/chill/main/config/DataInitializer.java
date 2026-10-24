package in.chill.main.config;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import in.chill.main.entity.Admin;
import in.chill.main.entity.Club;
import in.chill.main.entity.Department;
import in.chill.main.entity.Events;
import in.chill.main.entity.Judge;
import in.chill.main.entity.User;
import in.chill.main.entity.Venue;
import in.chill.main.repository.AdminRepository;
import in.chill.main.repository.ClubRepository;
import in.chill.main.repository.DepartmentRepository;
import in.chill.main.repository.EventsRepository;
import in.chill.main.repository.JudgeRepository;
import in.chill.main.repository.UserRepository;
import in.chill.main.repository.VenueRepository;

/**
 * Auto-seeds essential master data (Admin, Test User, Clubs, Venues, Events)
 * if the connected database is empty. Ensures the app is immediately usable on Supabase.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            seedAdmins();
            seedUsers();
            seedClubsVenuesDepartmentsJudgesAndEvents();
        } catch (Exception e) {
            logger.warn("DataInitializer encountered a non-fatal warning during seed check: {}", e.getMessage());
        }
    }

    private void seedAdmins() {
        var adminOpt = adminRepository.findByEmail("admin@admin.org");
        if (adminOpt.isEmpty()) {
            Admin admin = new Admin();
            admin.setName("System Admin");
            admin.setEmail("admin@admin.org");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            adminRepository.save(admin);
            logger.info(" Default admin created: admin@admin.org / admin123");
        } else {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches("admin123", admin.getPassword())) {
                admin.setPassword(passwordEncoder.encode("admin123"));
                adminRepository.save(admin);
                logger.info(" Default admin password verified/synced: admin@admin.org / admin123");
            }
        }
    }

    private void seedUsers() {
        var userOpt = userRepository.findByEmail("student@festflex.com");
        if (userOpt.isEmpty()) {
            User student = new User();
            student.setEmail("student@festflex.com");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setFirstName("Demo");
            student.setLastName("Student");
            student.setCollege("FestFlex University");
            student.setContact("9876543210");
            student.setRole("STUDENT");
            userRepository.save(student);
            logger.info(" Default student created: student@festflex.com / student123");
        } else {
            User student = userOpt.get();
            if (!passwordEncoder.matches("student123", student.getPassword())) {
                student.setPassword(passwordEncoder.encode("student123"));
                userRepository.save(student);
                logger.info(" Default student password verified/synced: student@festflex.com / student123");
            }
        }
    }

    private void seedClubsVenuesDepartmentsJudgesAndEvents() {
        Club codingClub = null;
        if (clubRepository.count() == 0) {
            codingClub = clubRepository.save(new Club("Coding Club", "Alex Turner", "9876543211", "alex@codingclub.org"));
            clubRepository.save(new Club("Robotics Club", "Sophia Chen", "9876543212", "sophia@robotics.org"));
            clubRepository.save(new Club("Design & Media", "Liam Patel", "9876543213", "liam@designclub.org"));
            logger.info(" Default clubs seeded.");
        } else {
            codingClub = clubRepository.findAll().stream().findFirst().orElse(null);
        }

        Venue mainAuditorium = null;
        if (venueRepository.count() == 0) {
            mainAuditorium = venueRepository.save(new Venue("Main Auditorium", 1));
            venueRepository.save(new Venue("Tech Lab Alpha", 2));
            venueRepository.save(new Venue("Seminar Hall C", 3));
            logger.info(" Default venues seeded.");
        } else {
            mainAuditorium = venueRepository.findAll().stream().findFirst().orElse(null);
        }

        if (departmentRepository.count() == 0) {
            departmentRepository.save(new Department("Computer Science & Engineering"));
            departmentRepository.save(new Department("Information Technology"));
            departmentRepository.save(new Department("Electronics & Communication"));
            logger.info(" Default departments seeded.");
        }

        Judge chiefJudge = null;
        if (judgeRepository.count() == 0) {
            chiefJudge = judgeRepository.save(new Judge("Dr. Rajesh Sharma"));
            judgeRepository.save(new Judge("Prof. Priya Nair"));
            logger.info(" Default judges seeded.");
        } else {
            chiefJudge = judgeRepository.findAll().stream().findFirst().orElse(null);
        }

        if (eventsRepository.count() == 0) {
            Integer clubId = (codingClub != null) ? codingClub.getClub_id() : 1;
            Integer venueId = (mainAuditorium != null) ? mainAuditorium.getVenue_id() : 1;
            Integer judgeId = (chiefJudge != null) ? chiefJudge.getJudge_id() : 1;

            Events hackathon = new Events();
            hackathon.setEvent_name("HackFest 2026");
            hackathon.setEvent_start_date("2026-11-15");
            hackathon.setEvent_end_date("2026-11-17");
            hackathon.setEvent_time("09:00 AM - 05:00 PM");
            hackathon.setEvent_type("Hackathon");
            hackathon.setEvent_description("24-hour national level coding hackathon featuring AI, Web3 and Cloud innovation tracks.");
            hackathon.setClub_id(clubId);
            hackathon.setVenue_id(venueId);
            hackathon.setJudge_id(judgeId);
            eventsRepository.save(hackathon);

            Events roboWars = new Events();
            roboWars.setEvent_name("RoboWars Arena");
            roboWars.setEvent_start_date("2026-11-20");
            roboWars.setEvent_end_date("2026-11-21");
            roboWars.setEvent_time("10:00 AM - 04:00 PM");
            roboWars.setEvent_type("Robotics");
            roboWars.setEvent_description("Combat robotics tournament featuring custom bots, wired and wireless obstacle navigations.");
            roboWars.setClub_id(clubId);
            roboWars.setVenue_id(venueId);
            roboWars.setJudge_id(judgeId);
            eventsRepository.save(roboWars);

            Events codeSprint = new Events();
            codeSprint.setEvent_name("CodeSprint 3.0");
            codeSprint.setEvent_start_date("2026-12-05");
            codeSprint.setEvent_end_date("2026-12-05");
            codeSprint.setEvent_time("02:00 PM - 06:00 PM");
            codeSprint.setEvent_type("Competitive Coding");
            codeSprint.setEvent_description("Fast-paced algorithmic programming contest with prizes for top speed and efficiency.");
            codeSprint.setClub_id(clubId);
            codeSprint.setVenue_id(venueId);
            codeSprint.setJudge_id(judgeId);
            eventsRepository.save(codeSprint);

            logger.info(" Default upcoming events seeded successfully.");
        }
    }
}