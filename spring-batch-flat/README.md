# 배치 어플리케이션 예제

- csv파일에서 데이터를 읽어 DB에 저장
- type별로 그루핑하여 각 서버에 HTTP 프로토콜로 전송
  - type이 1이면 1번 서버(/api/v1/products/1)
  - type이 2면 2번 서버(/api/v1/products/2)
  - type이 3이면 3번 서버(/api/v1/products/3)

## 설정

### 1. H2 데이터베이스 서버모드로 기동
- url: jdbc:h2:tcp://localhost/~/batch
- username: sa
- password: 

### 2. module-api 기동

### 3. module-batch 

하기 순서로 Batch Job 실행
- --job.name=fileJob requestDate=20210101
- --job.name=fileJob requestDate=20210102
- --job.name=apiJob
