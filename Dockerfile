FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/volunteer-app-backend-0.0.1-SNAPSHOT.jar volunteer-app-backend.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "volunteer-app-backend.jar"]
