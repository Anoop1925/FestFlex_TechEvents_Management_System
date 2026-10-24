# Multi-stage build for FestFlex Spring Boot Backend
# Stage 1: Build using Maven and JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the generated executable WAR
COPY --from=build /app/target/ChillDude-1.0.war app.war

# Optimized JVM memory limits for 512MB RAM free tier (Render / Railway / Koyeb)
ENV JAVA_OPTS="-Xmx350m -Xms128m -Xss512k -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.war"]
