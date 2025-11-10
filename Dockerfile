# Use stable JDK 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven config and source
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Download dependencies
RUN mvn dependency:go-offline

# Build the JAR
RUN mvn -B -DskipTests clean package spring-boot:repackage


# Expose the application port
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]

