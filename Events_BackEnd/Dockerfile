# Stage 1: Build
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy WAR file
COPY target/ChillDude-1.0.war app.war

# Expose port (Render automatically maps this)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.war"]
