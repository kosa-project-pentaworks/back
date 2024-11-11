# 1. Node.js 기반 React 빌드
FROM node:20 AS react-build
WORKDIR /frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install --production
COPY frontend ./
RUN npm run build

# 2. Spring Boot 빌드
FROM openjdk:17-alpine AS spring-boot-build
WORKDIR /build
COPY build/libs/*.jar app.jar

# 3. 최종 통합
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=react-build /frontend/build ./resources/static
COPY --from=spring-boot-build /build/app.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
