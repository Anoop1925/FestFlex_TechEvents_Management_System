# FestFlex - Supabase Database Integration & Setup Guide

This guide details everything about the project architecture, why the backend server crashed previously, and how to seamlessly connect FestFlex to a **Supabase PostgreSQL** database.

---

## 1. What Happened & Why Did the Server Crash on Render?

When inspecting the repository thoroughly, 4 major root causes were identified that caused the server crashes and database failures:

1. **Render Free-Tier Database Expiration**:
   - Render's free tier PostgreSQL database (`dpg-d3fpbh3ipnbc73b3cu9g-a...render.com`) expires and deletes itself automatically after 30 days.
   - When the database expired, Spring Boot failed to obtain a JDBC connection on startup and crashed with `Unable to obtain JDBC Connection`.

2. **Procfile Artifact Mismatch**:
   - The backend `Procfile` was configured with:
     `web: java -jar -Dserver.port=$PORT target/ChillDude-1.0.jar`
   - However, `pom.xml` was set to `<packaging>war</packaging>`, which built `target/ChillDude-1.0.war`.
   - When Render or any cloud runner executed the Procfile, Java failed immediately with `Error: Unable to access jarfile target/ChillDude-1.0.jar`, causing an immediate crash loop.

3. **Tomcat `provided` Scope Conflict**:
   - `spring-boot-starter-tomcat` had `<scope>provided</scope>` (intended for external WildFly servers).
   - As a result, the packaged application lacked an embedded servlet container, preventing standalone execution.

4. **Case Sensitivity & Native Query Mismatch**:
   - `Events.java` entity used `@Table(name = "Events")` (capital `E`) combined with Hibernate's `globally_quoted_identifiers=true`.
   - In PostgreSQL, quoted `"Events"` creates a case-sensitive table name.
   - However, all native SQL queries in `EventsRepository` and `ClubRepository` run queries like `SELECT * FROM events` (lowercase). This threw `ERROR: relation "events" does not exist`.

---

## 2. Fixes Applied in the Codebase

✅ **Fixed Table Name**: Updated `Events.java` to `@Table(name = "events")` to maintain full consistency with native queries and PostgreSQL conventions.
✅ **Safe Date Casting**: Updated `EventsRepository.java` to safeguard date queries against null or empty strings before `::DATE` casting.
✅ **Executable WAR Packaging**: Removed `<scope>provided</scope>` from Tomcat so the application packages as a self-contained executable archive with embedded Tomcat bundled inside `BOOT-INF/`.
✅ **Procfile Synced**: Updated `Procfile` to point to `target/ChillDude-1.0.war`.
✅ **Supabase HikariCP Pool**: Added connection pooling configuration in `application.properties` with keep-alive, connection test queries, and connection timeouts tailored for cloud PostgreSQL.
✅ **Automatic Master Data Seeder (`DataInitializer.java`)**: Added an automated startup seeder. If the connected database is empty, it automatically seeds:
   - Default Admin (`admin@admin.org` / `admin123`)
   - Demo Student (`student@festflex.com` / `student123`)
   - Default Clubs, Venues, Departments, Judges, and upcoming 2026 Events for the homepage timer.
✅ **Complete SQL DDL & Seed Script (`supabase-schema.sql`)**: Generated full SQL for all 15 tables and seed data to run in Supabase's SQL Editor with one click.

---

## 3. How to Set Up Supabase for FestFlex (Step-by-Step)

Supabase provides a standard cloud PostgreSQL instance that will keep your data safe, persistent, and accessible from anywhere.

### Step 1: Create a Free Supabase Project
1. Go to [https://supabase.com](https://supabase.com) and sign in (via GitHub or email).
2. Click **New project**.
3. Choose an Organization, enter a project name (e.g., `FestFlex-DB`), set a secure **Database Password** (save this password!), and pick the nearest region (e.g., `South Asia (Mumbai)`).
4. Click **Create new project** and wait ~1 minute for provisioning.

### Step 2: Get Your JDBC Connection Details
1. In your Supabase Dashboard, go to **Project Settings** (gear icon) -> **Database**.
2. Scroll to **Connection string** and select the **URI** or **Parameters** tab.
3. Look for the **Connection Pooling** (Supavisor) section:
   - **Host**: e.g., `aws-0-ap-south-1.pooler.supabase.com`
   - **Port**: `6543` (Transaction mode) or `5432` (Session mode)
   - **Database**: `postgres`
   - **User**: `postgres.[PROJECT-REF]` (e.g., `postgres.abcdefghijklmno`)
   - **Password**: `[YOUR_DB_PASSWORD]`
   
   *Tip*: Using the **Session Pooler** (Port `5432` or `6543`) on `aws-0-*.pooler.supabase.com` is recommended because it supports IPv4 networks universally.

### Step 3: Run the Schema in Supabase (Recommended)
1. In your Supabase Dashboard, open the **SQL Editor** tab from the left sidebar.
2. Click **New query**.
3. Open the file [supabase-schema.sql](file:///c:/Projects/FSD_Events/FestFlex_TechEvents_Management_System/supabase-schema.sql) in your project root.
4. Copy the entire content, paste it into the Supabase SQL editor, and click **Run** (green button).
5. All 15 tables (`users`, `admins`, `events`, `club`, `venue`, `department`, `judge`, `sponsor`, `budget`, `registration`, `participation`, `result`, `volunteer`, `feedback`, `comments`) will be created with sample data!

*(Note: Even if you skip this step, Spring Boot's Hibernate will auto-create the tables on startup via `ddl-auto=update` and `DataInitializer` will seed the default admin).*

---

## 4. Connecting the Backend to Supabase

You can connect either via `application.properties` or via environment variables.

### Option A: Via `application.properties`
In `Events_BackEnd/src/main/resources/application.properties`, update the datasource lines:
```properties
spring.datasource.url=jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require
spring.datasource.username=postgres.<PROJECT_REF>
spring.datasource.password=<YOUR_DATABASE_PASSWORD>
```
*(Replace `<POOLER_HOST>`, `<PROJECT_REF>`, and `<YOUR_DATABASE_PASSWORD>` with your actual Supabase credentials).*

### Option B: Via Environment Variables (Best for Cloud Deployment / Local Terminals)
```bash
# Windows PowerShell
$env:DATABASE_URL="jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require"
$env:DATABASE_USERNAME="postgres.<PROJECT_REF>"
$env:DATABASE_PASSWORD="<YOUR_DATABASE_PASSWORD>"
```

---

## 5. Running and Testing Locally

### Start Backend
```powershell
cd c:\Projects\FSD_Events\FestFlex_TechEvents_Management_System\Events_BackEnd
.\mvnw.cmd spring-boot:run
```
The server will start at `http://localhost:8080`.

### Start Frontend
```powershell
cd c:\Projects\FSD_Events\FestFlex_TechEvents_Management_System\Angular_Frontend\events-management-app
npm start
```
The client will start at `http://localhost:4200`.

---

## 6. Default Login Credentials

| Role | Email | Password | Access Route |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@admin.org` | `admin123` | `/auth/admin-login` -> `/admin/dashboard` |
| **Student** | `student@festflex.com` | `student123` | `/auth/student-login` -> Home / Events |
