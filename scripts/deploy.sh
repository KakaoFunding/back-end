#!/bin/bash
set -e

ROOT_PATH="/home/ec2-user/cicd"
DOCKERFILE_PATH="$ROOT_PATH/Dockerfile"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

IMAGE_NAME="application"
IMAGE_TAG="latest"

START_LOG="$ROOT_PATH/start.log"
BUILD_LOG="$ROOT_PATH/build.log"

touch $START_LOG $BUILD_LOG

echo "[$(date +%c)] Gradle 빌드 시작" >> $START_LOG
cd $ROOT_PATH || exit

sudo chown ec2-user:ec2-user ./gradlew
sudo chmod +x ./gradlew

./gradlew clean
./gradlew build -i -x test >> $BUILD_LOG 2>&1

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Gradle 빌드 성공" >> $START_LOG

    echo "[$(date +%c)] Docker 이미지 빌드 시작" >> $START_LOG
    docker build -t $IMAGE_NAME:$IMAGE_TAG -f $DOCKERFILE_PATH $ROOT_PATH >> $BUILD_LOG 2>&1

    if [ $? -eq 0 ]; then
        echo "[$(date +%c)] Docker 이미지 빌드 성공: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG

        echo "[$(date +%c)] Docker Compose로 애플리케이션 시작" >> $START_LOG
        export SPRING_PROFILES_ACTIVE=prod
        docker-compose -f $DOCKER_COMPOSE_PATH up -d >> $BUILD_LOG 2>&1

        if [ $? -eq 0 ]; then
            echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
        else
            echo "[$(date +%c)] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
        fi
    else
        echo "[$(date +%c)] Docker 이미지 빌드 실패: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG
    fi
else
    echo "[$(date +%c)] Gradle 빌드 실패" >> $START_LOG
fi
