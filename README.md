# hospitalGo.

## 개요
한국SW산업협회
MSA Full Stack 8차

개발 기간: 2024.10.04 ~ 2024.11.27

배포 주소: http://52.79.220.59/

팀 소개
* 정현목 (팀장): 서비스 제공의 필요한 전반적인 배치성 작업, 결제 동시성 제어
* 박상준: 회원 인증과 시스템 아키텍처 설계 및 인프라 구축
* 이원준: 관리자

공공데이터 기반의 프로젝트로서, 공공데이터의 신뢰성을 활용해 고객 요구를 신속히 반영하고, 맞춤형 서비스를 제공하는 과정을 경험하고자 주제를 선택하였습니다.

## 주요 기술 스택
* Spring Boot 3.x
* Java 17
* Spring Security 6.x
* Spring Batch 5.x
* JWT
* OAuth 2.0 (카카오 소셜 로그인)
* Gradle
* MySQL
* Docker
* React 18
* Node 20.9.0

## 프로젝트 설정
### 사전준비
1. Java 17 또는 그 이상의 버전을 설치합니다.
2. Node 20.9.0 버전을 설치합니다.
3. 로컬 개발 환경에서는 Docker 를 활용하여 MySQL, Redis 를 설치합니다.

#### 노드 설치
nvm 을 활용하여 노드를 설치할 수 있습니다.

1. NVM 설치 확인
먼저, nvm 이 설치되어 있는지 확인합니다. 터미널에 아래 명령어를 입력하여 nvm 버전을 확인합니다.
```
nvm --version
```

nvm 이 설치되어 있지 않다면, nvm 공식 GitHub 페이지에서 설치 방법을 참고하세요.

2. Node.js 20.9.0 버전 설치
nvm 이 설치되었다면, 아래 명령어를 입력하여 Node.js 20.9.0 버전을 설치합니다.
```
nvm install 20.9.0
```

이 명령어는 Node.js 20.9.0 버전을 다운로드하고 설치합니다. 설치 후 자동으로 해당 버전이 활성화됩니다.

3. 설치된 Node.js 버전 확인
설치된 Node.js 버전을 확인하려면 다음 명령어를 사용하세요.
```
node -v
```
v20.9.0 이 출력되면 설치가 정상적으로 완료된 것입니다.

#### Docker-compose 를 활용하여 MySQL, Redis 실행
```
docker-compose -f docker-compose.yml up -d

docker ps -a
```
## 프로젝트 실행
### 백엔드 애플리케이션 실행
`ReservationApplication` 을 실행합니다.

http://localhost:8080 으로 접근할 수 있습니다.

### 프론트엔드 애플리케이션 실행
`frontend` 디렉토리로 접근하여 애플리케이션을 실행합니다.

```
cd ./frontend

npm run start
```

http://localhost:3000 으로 접근합니다.

## ERD (Entity-Relation Diagram)
![image](https://github.com/user-attachments/assets/a4007df8-a381-48ba-b998-5a1ec7d14ab8)


## 시스템 아키텍처

![image](https://github.com/user-attachments/assets/d0935126-9448-47a3-b8e9-339ac21242bb)
