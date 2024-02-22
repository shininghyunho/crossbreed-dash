# gradle, jdk17 이미지를 기반으로 빌드
FROM gradle:8.5-jdk17

# 소스 코드 복사
COPY . /home/app/src

# 작업 디렉토리 설정
WORKDIR /home/app/src

# dos2unix 설치 및 gradlew 파일 변환
RUN apt-get update && apt-get install -y dos2unix && dos2unix gradlew

# 포트 설정
EXPOSE 25565

# 클라이언트 실행
CMD ["./gradlew", "runClient"]