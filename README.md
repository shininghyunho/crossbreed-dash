# 개요
- 말을 교배하고 부여되는 능력으로 경마를 하는 게임입니다.

# 게임 규칙
- /start 명령어를 통해 게임을 시작합니다.
- 자연에있는 말에 올라타면 말에 이름과 능력이 부여됩니다.
- 특정 시간마다 경주가 시작됩니다.
- 경주 준비 단계가 되면 출발선에 대기합니다.
- 경주가 시작되면 주어진 트랙에 따라 경주를 합니다.
- 경주가 끝나면 순위가 매겨지고 점수가 누적됩니다.
- 경주가 끝나면 다시 준비 단계로 돌아가고 다음 경주를 준비합니다.
- /breed horesName1 horesName2 명령어를 통해 두 말을 교배합니다.
- 교배한 말은 더 좋은 능력을 가질 수 있습니다.
- /stop 명령어를 통해 게임을 종료합니다.

# 게임 명령어
- /start: 게임을 시작합니다.
- /breed horesName1 horesName2: 두 말을 교배합니다.
- /stop: 게임을 종료합니다.

# 게임 환경
- java 17+
- minecraft 1.20.1
- fabric 1.20.1

# 게임 설치
- fabric 1.20.1을 설치합니다.
- git clone https://github.com/shininghyunho/crossbreed-dash.git
- ./gradlew runClient
```