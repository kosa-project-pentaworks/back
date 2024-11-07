# Step 1: Build Stage
FROM gradle:7.4.2-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Step 2: Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]