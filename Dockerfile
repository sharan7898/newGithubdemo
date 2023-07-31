# Use the official OpenJDK image for the runtime image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory to the container's working directory
COPY nysf-kheloindia-youth-dev-v3.0/target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar /app/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar

# Run the application
CMD ["java", "-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]



