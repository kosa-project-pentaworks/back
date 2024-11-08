# Step 1: Build Stage
FROM gradle:7.4.2-jdk17 AS build
WORKDIR /app

# Gradle wrapper 및 종속성 파일 복사
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/

# Gradle 종속성 캐싱
RUN ./gradlew dependencies --no-daemon

# 애플리케이션 코드 복사 및 빌드 실행
COPY . /app
RUN ./gradlew bootJar --no-daemon

# Step 2: Runtime Stage
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
