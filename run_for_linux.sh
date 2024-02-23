#!/bin/bash

# 1. Gradle을 사용하여 프로젝트를 빌드.
./gradlew build

# 2. Docker Compose를 사용하여 서비스를 시작.
docker-compose up -d

# 3. 로그 확인.
docker-compose logs -f