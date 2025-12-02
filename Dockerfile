FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file from target folder
COPY target/*.jar app.jar

# Expose the app port (optional)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
