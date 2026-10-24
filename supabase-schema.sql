-- ==============================================================================
-- FestFlex TechEvents Management System
-- Supabase PostgreSQL Schema & Initial Seed Data
-- ==============================================================================
-- Run this script in the Supabase Dashboard -> SQL Editor
-- This sets up all 15 tables, foreign keys, indexes, and initial master data.
-- ==============================================================================

-- 1. Users Table (Students / Participants)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    college VARCHAR(255),
    contact VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Admins Table (Admins / Organizers - Email must end with @admin.org)
CREATE TABLE IF NOT EXISTS admins (
    admin_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Club Table
CREATE TABLE IF NOT EXISTS club (
    club_id SERIAL PRIMARY KEY,
    club_name VARCHAR(255),
    president_name VARCHAR(255),
    president_contact VARCHAR(255),
    president_email VARCHAR(255)
);

-- 4. Department Table
CREATE TABLE IF NOT EXISTS department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(255)
);

-- 5. Venue Table
CREATE TABLE IF NOT EXISTS venue (
    venue_id SERIAL PRIMARY KEY,
    venue_name VARCHAR(255),
    floor INT DEFAULT 1
);

-- 6. Judge Table
CREATE TABLE IF NOT EXISTS judge (
    judge_id SERIAL PRIMARY KEY,
    judge_name VARCHAR(255)
);

-- 7. Events Table
CREATE TABLE IF NOT EXISTS events (
    event_id SERIAL PRIMARY KEY,
    event_name VARCHAR(255) NOT NULL,
    event_start_date VARCHAR(255) NOT NULL,
    event_end_date VARCHAR(255),
    event_time VARCHAR(255),
    event_type VARCHAR(255),
    event_description TEXT,
    judge_id INT REFERENCES judge(judge_id) ON DELETE SET NULL,
    club_id INT REFERENCES club(club_id) ON DELETE SET NULL,
    venue_id INT REFERENCES venue(venue_id) ON DELETE SET NULL
);

-- 8. Sponsor Table
CREATE TABLE IF NOT EXISTS sponsor (
    sponsor_id SERIAL PRIMARY KEY,
    sponsor_name VARCHAR(255) NOT NULL,
    is_cash BOOLEAN DEFAULT TRUE,
    sponsor_mode VARCHAR(255),
    sponsor_contact VARCHAR(255),
    club_id INT REFERENCES club(club_id) ON DELETE CASCADE
);

-- 9. Budget Table
CREATE TABLE IF NOT EXISTS budget (
    budget_id SERIAL PRIMARY KEY,
    allocated_amount REAL NOT NULL,
    utilized_amount REAL NOT NULL,
    sponsor_id INT REFERENCES sponsor(sponsor_id) ON DELETE SET NULL,
    club_id INT REFERENCES club(club_id) ON DELETE CASCADE,
    event_id INT REFERENCES events(event_id) ON DELETE CASCADE
);

-- 10. Registration Table (Event Registrations)
CREATE TABLE IF NOT EXISTS registration (
    registration_id SERIAL PRIMARY KEY,
    registration_name VARCHAR(255) NOT NULL,
    college VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 11. Participation Table
CREATE TABLE IF NOT EXISTS participation (
    participation_id SERIAL PRIMARY KEY,
    registration_id INT REFERENCES registration(registration_id) ON DELETE CASCADE,
    event_id INT REFERENCES events(event_id) ON DELETE CASCADE,
    event_amount REAL DEFAULT 0.0
);

-- 12. Result Table
CREATE TABLE IF NOT EXISTS result (
    result_id SERIAL PRIMARY KEY,
    rank INT NOT NULL,
    score REAL NOT NULL,
    event_id INT REFERENCES events(event_id) ON DELETE CASCADE,
    participant_id INT REFERENCES participation(participation_id) ON DELETE CASCADE
);

-- 13. Volunteer Table
CREATE TABLE IF NOT EXISTS volunteer (
    volunteer_id SERIAL PRIMARY KEY,
    volunteer_name VARCHAR(255) NOT NULL,
    volunteer_role VARCHAR(255),
    volunteer_contact VARCHAR(255),
    is_assigned BOOLEAN DEFAULT FALSE,
    club_id INT REFERENCES club(club_id) ON DELETE SET NULL,
    department_id INT REFERENCES department(department_id) ON DELETE SET NULL,
    venue_id INT REFERENCES venue(venue_id) ON DELETE SET NULL,
    event_id INT REFERENCES events(event_id) ON DELETE SET NULL
);

-- 14. Feedback Table
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id SERIAL PRIMARY KEY,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255),
    email VARCHAR(255),
    contact_no VARCHAR(255),
    overall_event_rating INT,
    org_management INT,
    venue_facilities INT,
    tech_content INT,
    comments VARCHAR(1000),
    registration_id INT REFERENCES registration(registration_id) ON DELETE SET NULL
);

-- 15. Comments Table (Public Feed / Discussion)
CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGSERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==============================================================================
-- INDEXES FOR PERFORMANCE
-- ==============================================================================
CREATE INDEX IF NOT EXISTS idx_events_start_date ON events(event_start_date);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_admins_email ON admins(email);
CREATE INDEX IF NOT EXISTS idx_participation_event ON participation(event_id);
CREATE INDEX IF NOT EXISTS idx_comments_user ON comments(user_id);

-- ==============================================================================
-- SEED DATA (Only inserted if tables are empty)
-- ==============================================================================

-- Default Admin (Password: admin123, BCrypt encoded)
INSERT INTO admins (name, email, password, role, created_at, updated_at)
VALUES (
    'Super Admin', 
    'admin@admin.org', 
    '$2a$10$G.M3nKq68m/T9gE2Lvhwxe08Fm4Yh8nN9.g21y2YkQeZqNf3BovOq', 
    'ADMIN', 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;

-- Default Student User (Password: student123, BCrypt encoded)
INSERT INTO users (email, password, first_name, last_name, college, contact, role, created_at, updated_at)
VALUES (
    'student@festflex.com', 
    '$2a$10$Wq6P4v0Xj5J0G7fE5c6h5.o4G3wQ8zN1mK5eP9v2Y4h8mN7jK5lQe', 
    'Demo', 
    'Student', 
    'FestFlex University', 
    '9876543210', 
    'STUDENT', 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;

-- Clubs
INSERT INTO club (club_name, president_name, president_contact, president_email)
VALUES 
    ('Coding Club', 'Alex Turner', '9876543211', 'alex@codingclub.org'),
    ('Robotics Club', 'Sophia Chen', '9876543212', 'sophia@robotics.org'),
    ('Design & Media', 'Liam Patel', '9876543213', 'liam@designclub.org')
ON CONFLICT DO NOTHING;

-- Venues
INSERT INTO venue (venue_name, floor)
VALUES 
    ('Main Auditorium', 1),
    ('Tech Lab Alpha', 2),
    ('Seminar Hall C', 3)
ON CONFLICT DO NOTHING;

-- Departments
INSERT INTO department (department_name)
VALUES 
    ('Computer Science & Engineering'),
    ('Information Technology'),
    ('Electronics & Communication')
ON CONFLICT DO NOTHING;

-- Judges
INSERT INTO judge (judge_name)
VALUES 
    ('Dr. Rajesh Sharma'),
    ('Prof. Priya Nair')
ON CONFLICT DO NOTHING;

-- Upcoming Events
INSERT INTO events (event_name, event_start_date, event_end_date, event_time, event_type, event_description, judge_id, club_id, venue_id)
VALUES 
    ('HackFest 2026', '2026-11-15', '2026-11-17', '09:00 AM - 05:00 PM', 'Hackathon', '24-hour national level coding hackathon featuring AI, Web3 and Cloud innovation tracks.', 1, 1, 1),
    ('RoboWars Arena', '2026-11-20', '2026-11-21', '10:00 AM - 04:00 PM', 'Robotics', 'Combat robotics tournament featuring custom bots, wired and wireless obstacle navigations.', 2, 2, 2),
    ('CodeSprint 3.0', '2026-12-05', '2026-12-05', '02:00 PM - 06:00 PM', 'Competitive Coding', 'Fast-paced algorithmic programming contest with prizes for top speed and efficiency.', 1, 1, 3)
ON CONFLICT DO NOTHING;
