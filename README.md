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
Table TB_USER_MASTER {
  user_id BIGINT [pk, not null]
  provider_id VARCHAR(255)
  email VARCHAR(100)
  username VARCHAR(50)
  phone VARCHAR(11)
  role VARCHAR(20) [not null]
  provider VARCHAR(20)
  status VARCHAR(20) [not null]
  zipcode VARCHAR(20)
  address_line1 VARCHAR(50)
  address_line2 VARCHAR(50)
  city VARCHAR(50)
  state VARCHAR(50)
  created_at DATETIME [not null]
  created_by VARCHAR(50) [not null]
  modified_at DATETIME [not null]
  modified_by VARCHAR(50) [not null]
}

Table TB_HOSPITAL_MASTER {
  hosp_id BIGINT [pk, not null]
  cl_cd INT
  addr VARCHAR(150) [not null]
  cl_cd_nm VARCHAR(16) [not null]
  emdong_nm VARCHAR(16)
  estb_dd VARCHAR(10)
  hosp_url VARCHAR(100)
  post_no INT [not null]
  sggu_cd INT [not null]
  sggu_cd_nm VARCHAR(20) [not null]
  sido_cd INT [not null]
  sido_cd_nm VARCHAR(10) [not null]
  telno VARCHAR(15)
  yadm_nm VARCHAR(60) [not null]
  ykiho VARCHAR(255) [not null]
  hosp_status VARCHAR(5) [not null]
}

Table TB_HOSPITAL_RESERVATION_HISTORY {
  hosp_reservation_id BIGINT [pk, not null]
  reservation_at DATETIME [not null]
  reservation_time VARCHAR(10) [not null]
  created_at DATETIME [not null]
  modified_at DATETIME
  reservation_status VARCHAR(20) [not null]
  user_id BIGINT [not null, ref: > TB_USER_MASTER.user_id]
  payment_id BIGINT [not null]
  hosp_id BIGINT [not null, ref: > TB_HOSPITAL_MASTER.hosp_id]
}

Table TB_PAYMENT_HISTORY {
  payment_id BIGINT [pk, not null]
  userid BIGINT [not null, ref: > TB_USER_MASTER.user_id]
  hosp_id BIGINT [not null, ref: > TB_HOSPITAL_MASTER.hosp_id]
  import_uid VARCHAR(200) [not null]
  payment_status VARCHAR(12) [not null]
  amount INT [not null]
  created_at DATETIME [not null]
  modified_at DATETIME
}

Table TB_USER_TOKEN {
  token_id VARCHAR(255) [pk, not null]
  user_id BIGINT [not null, ref: > TB_USER_MASTER.user_id]
  access_token VARCHAR(255) [not null]
  refresh_token VARCHAR(255) [not null]
  access_token_expires_at DATETIME [not null]
  refresh_token_expires_at DATETIME [not null]
  created_at DATETIME [not null]
  created_by VARCHAR(50) [not null]
  modified_at DATETIME [not null]
  modified_by VARCHAR(50) [not null]
}

## 시스템 아키텍처

![image](https://github.com/user-attachments/assets/d0935126-9448-47a3-b8e9-339ac21242bb)
