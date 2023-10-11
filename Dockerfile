# Use a suitable base image with JDK (OpenJDK 8)
FROM openjdk:8

# Set the working directory for the application
WORKDIR /app

# Copy the application source code and .env file to the container
COPY . .

# Build the JVM executable
RUN ./gradlew installDist

# Expose the port your Ktor application will listen on (replace with your actual port)
EXPOSE 8080

# Run the application
CMD ./build/install/com.coffee_service.quadro.org.manufacture_service/bin/com.coffee_service.quadro.org.manufacture_service
