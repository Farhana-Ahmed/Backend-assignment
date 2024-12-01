
FROM openjdk:17-jdk-slim
COPY target/backend-assignment-sandbox-0.0.1-SNAPSHOT.jar backend-assignment-sandbox.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-assignment-sandbox.jar"]