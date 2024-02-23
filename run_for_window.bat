@echo off

REM 1. Gradle을 사용하여 프로젝트를 빌드.
call gradlew.bat build

REM 2. Docker Compose를 사용하여 서비스를 시작.
call docker-compose up -d

REM 3. 로그 확인.
call docker-compose logs -f