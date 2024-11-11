# Stage 1: Build Frontend (멀티스테이지 빌드)
FROM node:20 as build

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사 후 의존성 설치
COPY package.json package-lock.json ./
RUN npm install

# 애플리케이션 파일 복사 후 빌드
COPY . .
RUN npm run build

# Stage 2: Nginx 이미지 생성
FROM nginx:alpine

# 빌드된 파일을 Nginx의 기본 제공 html 디렉토리로 복사
COPY --from=build /app/build /usr/share/nginx/html

# Nginx 기본 포트
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
