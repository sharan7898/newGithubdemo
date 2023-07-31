FROM openjdk:11-jre-slim
WORKDIR /app
COPY nysf-kheloindia-youth-dev-v3.0/target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar /app/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]




