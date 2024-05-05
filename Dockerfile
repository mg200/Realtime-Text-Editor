# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

EXPOSE 8000

# The application's jar file
ARG JAR_FILE=target/app-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app-0.0.1-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","/app-0.0.1-SNAPSHOT.jar"]