# 1. Base image
FROM openjdk:17-alpine

# 2. Working directory 설정
WORKDIR /app

# 3. React 빌드 결과물 복사
COPY frontend/build /app/resources/static

# 4. Spring Boot JAR 복사
COPY build/libs/*.jar app.jar

# 5. 포트 설정
EXPOSE 8080

# 6. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
