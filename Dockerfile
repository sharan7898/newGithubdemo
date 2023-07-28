FROM maven:3.8.4-openjdk-11-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
COPY --from=build /app/target/nysf-kheloindia-youth.jar /app/maven-wrapper.jar
WORKDIR /app
CMD ["java", "-jar", "maven-wrapper.jar"]

