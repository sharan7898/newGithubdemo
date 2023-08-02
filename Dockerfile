FROM openjdk:17
WORKDIR /app
COPY target/nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar 
CMD ["java","-jar", "nysf-kheloindia-youth-0.0.1-SNAPSHOT.jar"]
