# Step 1 : 빌드하기
FROM gradle:8.5-jdk17 AS build

# 소스 코드 복사
COPY . /home/gradle/src

# 작업 디렉토리 설정
WORKDIR /home/gradle/src

# 빌드
RUN gradle build --no-daemon

# Step 2 : 실행하기
FROM openjdk:17-jdk-alpine3.13

# 작업 디렉토리 설정
RUN mkdir -p /home/app
WORKDIR /home/app

# 빌드된 파일 복사
COPY --from=build /home/gradle/src/build/libs/*.jar /home/app/app.jar

# 포트 설정
EXPOSE 25565

# 클라이언트 실행
CMD ["./gradlew", "runClient"]