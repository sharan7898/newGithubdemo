# Stage 1: Build the JAR file
FROM maven:3.8.1-jdk-11 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image with just the JAR file
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/nysf-kheloindia-youth-dev-v3.0/target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar ./nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]






