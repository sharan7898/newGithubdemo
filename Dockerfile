# Stage 1: Build the JAR file
# FROM maven:3.8.4-openjdk-11-slim AS builder
#WORKDIR /app
#COPY . /app
#RUN mvn clean package -DskipTests
#RUN mvn install

# Stage 2: Create a lightweight image with just the JAR file
FROM openjdk:11-jre-slim
WORKDIR /app
ENV MYSQL_PASSWORD=
ENV MYSQL_USERNAME=root
COPY target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar 
CMD ["java", "-Dspring.datasource.username=${MYSQL_USERNAME} ","spring.datasource.password=${MYSQL_PASSWORD}","-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]









