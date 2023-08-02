# Stage 1: Build the JAR file
# FROM maven:3.8.4-openjdk-11-slim AS builder
#WORKDIR /app
#COPY . /app
#RUN mvn clean package -DskipTests
#RUN mvn install

FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]









