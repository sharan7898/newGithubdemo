# Stage 1: Build the JAR file
FROM maven:3.8.1-jdk-11 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image with just the JAR file
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/nysf-kheloindia-youth.jar ./nysf-kheloindia-youth.jar
CMD ["java", "-jar", "nysf-kheloindia-youth.jar"]







