FROM maven:3.8.4-openjdk-11-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar newdemo.jar

# Run the application
CMD ["java", "-jar", "newdemo.jar"]


