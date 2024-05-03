#!/bin/bash

#!/bin/bash

# shellcheck disable=SC2164
cd /home/ec2-user/cicd

DOCKER_COMPOSE_FILE="docker-compose.yml"

# 파일 존재 확인 및 Docker Compose 실행
if [ -f "$DOCKER_COMPOSE_FILE" ]; then
    echo "docker-compose 파일을 찾았습니다. 컨테이너를 종료합니다."
    docker-compose -f "$DOCKER_COMPOSE_FILE" down
else
    echo "docker-compose.yml 파일을 찾을 수 없습니다. 현재 위치: $(pwd)"
    exit 1
fi