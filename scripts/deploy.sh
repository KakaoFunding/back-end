#!/bin/bash

ROOT_PATH="/home/ec2-user/cicd"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

if [ -z "$IMAGE_NAME" ]; then
    IMAGE_NAME="application"
    export IMAGE_NAME
fi

if [ -z "$IMAGE_TAG" ]; then
    IMAGE_TAG="latest"
    export IMAGE_TAG
fi

START_LOG="$ROOT_PATH/start.log"
BUILD_LOG="$ROOT_PATH/build.log"

touch $START_LOG $BUILD_LOG

echo "[$(date +%c)] Gradle 빌드 시작" >> $START_LOG
cd $ROOT_PATH || exit

chmod +x ./gradlew
./gradlew build -x test >> $BUILD_LOG 2>&1

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Gradle 빌드 성공" >> $START_LOG

    echo "[$(date +%c)] Docker 이미지 빌드 및 실행 시도" >> $START_LOG
    docker-compose -f $DOCKER_COMPOSE_PATH up -d >> $BUILD_LOG 2>&1
    if [ $? -eq 0 ]; then
        echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
    else
        echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
        echo "[$(date +%c)] Docker Compose 오류 로그:" >> $START_LOG
        docker-compose logs >> $START_LOG 2>&1
    fi
else
    echo "[$(date +%c)] Gradle 빌드 실패" >> $START_LOG
fi
