# 1. Node.js 기반 React 빌드
FROM node:20 AS react-build
WORKDIR /frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend ./
RUN npm run build

# 2. Java 기반 Spring Boot 애플리케이션 빌드
FROM openjdk:17-alpine AS spring-boot-build
WORKDIR /app
COPY build/libs/*.jar app.jar

# 3. 최종 이미지 생성
FROM openjdk:17-alpine
WORKDIR /app

# 3.1 React 빌드 결과 복사
COPY --from=react-build /frontend/build ./resources/static

# 3.2 Spring Boot JAR 복사
COPY --from=spring-boot-build /app/app.jar ./app.jar

# 4. 포트 설정
EXPOSE 8080

# 5. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
