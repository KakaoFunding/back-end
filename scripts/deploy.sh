#!/bin/bash

ROOT_PATH="/home/ec2-user/cicd"
DOCKERFILE_PATH="$ROOT_PATH/Dockerfile"
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

NOW=$(date +%c)

for PORT in 8081 8082 8083; do
    PID=$(lsof -ti:$PORT)
    if [ ! -z "$PID" ]; then
        echo "[$NOW] 포트 $PORT 사용중인 프로세스($PID) 종료" >> $START_LOG
        kill -9 $PID
    fi
done

echo "[$NOW] Docker 이미지 빌드 시작" >> $START_LOG
docker build -t $IMAGE_NAME:$IMAGE_TAG -f $DOCKERFILE_PATH $ROOT_PATH >> $BUILD_LOG 2>&1

if [ $? -eq 0 ]; then
    echo "[$NOW] Docker 이미지 빌드 성공: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG

    echo "[$NOW] Docker Compose를 사용하여 애플리케이션 시작" >> $START_LOG
    docker-compose -f $DOCKER_COMPOSE_PATH up -d

    if [ $? -eq 0 ]; then
        echo "[$NOW] Docker Compose로 애플리케이션 시작 성공" >> $START_LOG
    else
        echo "[$NOW] Docker Compose로 애플리케이션 시작 실패" >> $START_LOG
    fi
else
    echo "[$NOW] Docker 이미지 빌드 실패: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG
fi
