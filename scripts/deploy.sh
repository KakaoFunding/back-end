#!/bin/bash

ROOT_PATH="/home/ec2-user/cicd"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

if [ -z "$IMAGE_NAME" ]; then
    IMAGE_NAME="yeachan05/application"  # 여기에 Docker Hub 경로 작성
    export IMAGE_NAME
fi

if [ -z "$IMAGE_TAG" ]; then
    IMAGE_TAG=$(git rev-parse --short HEAD)
    export IMAGE_TAG
fi

START_LOG="$ROOT_PATH/start.log"
BUILD_LOG="$ROOT_PATH/build.log"

touch $START_LOG $BUILD_LOG

docker-compose -f $DOCKER_COMPOSE_PATH up -d

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
else
    echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
    echo "[$(date +%c)] Docker Compose 오류 로그:" >> $START_LOG
    docker-compose -f $DOCKER_COMPOSE_PATH logs >> $START_LOG 2>&1
fi
