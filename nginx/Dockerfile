# Stage 1: Build React Application
FROM node:20 as build

WORKDIR /app

# package.json과 package-lock.json 복사 후 의존성 설치
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install

# 애플리케이션 파일 복사 후 빌드
COPY frontend ./
RUN npm run build

# Stage 2: Nginx 이미지 생성
FROM nginx:alpine

# 빌드된 파일을 Nginx의 기본 제공 html 디렉토리로 복사
COPY --from=build /app/build /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/conf.d/nginx.conf

# Nginx 포트 설정
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
