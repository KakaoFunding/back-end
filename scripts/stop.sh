#!/bin/bash

#!/bin/bash

# 절대 경로를 사용하여 docker-compose 명령 실행
DOCKER_COMPOSE_FILE="/home/ec2-user/cicd/docker-compose.yml"

if [ -f "$DOCKER_COMPOSE_FILE" ]; then
    docker-compose -f "$DOCKER_COMPOSE_FILE" down
else
    echo "docker-compose.yml 파일을 찾을 수 없습니다."
fi
