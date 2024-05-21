#!/bin/bash

ROOT_PATH="/home/ec2-user/cicd"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

IMAGE_NAME="yeachan05/application"
IMAGE_TAG="latest"

START_LOG="$ROOT_PATH/start.log"
touch $START_LOG

echo "[$(date +%c)] Docker Compose 실행 시작 - 이미지: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG

docker-compose -f $DOCKER_COMPOSE_PATH pull
docker-compose -f $DOCKER_COMPOSE_PATH up -d

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
else
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
    echo "[$(date +%c)] Docker Compose 오류 로그:" >> $START_LOG
    docker-compose -f $DOCKER_COMPOSE_PATH logs >> $START_LOG 2>&1
fi
