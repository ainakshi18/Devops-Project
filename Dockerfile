FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file to the container
COPY target/Internship-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app is listening on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
