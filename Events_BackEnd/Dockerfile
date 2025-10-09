# Use Java 21 image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copy the rest of the code
COPY . .

# Build the WAR file inside Render's environment
RUN ./mvnw clean package -DskipTests

# Copy the WAR file into the container
COPY target/ChillDude-1.0.war app.war

# Expose port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.war"]
