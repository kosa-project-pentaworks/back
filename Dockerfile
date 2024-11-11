# React 빌드
FROM node:20 AS react-build
WORKDIR /frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend ./
RUN npm run build

# Spring Boot 빌드
FROM openjdk:17-alpine AS spring-boot-build
WORKDIR /build
COPY build/libs/*.jar app.jar

# 최종 통합
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=react-build /frontend/build ./resources/static
COPY --from=spring-boot-build /build/app.jar ./app.jar

# 캐시 정리
RUN rm -rf /var/cache/apk/*

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
