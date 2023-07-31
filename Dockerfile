# Use the official Maven image to build the application
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the entire project to the container
COPY . .

# Build the application with Maven (skip tests)
RUN mvn clean package -DskipTests

# Use the official OpenJDK image for the runtime image
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar nysf-kheloindia-youth.jar

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "nysf-kheloindia-youth.jar"]


