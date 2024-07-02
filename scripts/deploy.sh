#!/bin/bash

ROOT_PATH="/home/ec2-user/cicd"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

IMAGE_NAME="yeachan05/application"
IMAGE_TAG="latest"

START_LOG="$ROOT_PATH/start.log"
touch $START_LOG

echo "[$(date +%c)] Docker Compose 실행 시작 - 이미지: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG

# docker-compose pull 로그 기록
echo "[$(date +%c)] docker-compose pull 시작" >> $START_LOG
docker-compose -f $DOCKER_COMPOSE_PATH pull >> $START_LOG 2>&1

# docker-compose up 로그 기록
echo "[$(date +%c)] docker-compose up 시작" >> $START_LOG
docker-compose -f $DOCKER_COMPOSE_PATH up -d >> $START_LOG 2>&1

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
else
    # shellcheck disable=SC2129
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
    echo "[$(date +%c)] Docker Compose 오류 로그:" >> $START_LOG
    docker-compose -f $DOCKER_COMPOSE_PATH logs >> $START_LOG 2>&1
fi

# 전체 로그를 기록
echo "[$(date +%c)] 전체 Docker Compose 로그:" >> $START_LOG
docker-compose -f $DOCKER_COMPOSE_PATH logs >> $START_LOG 2>&1
