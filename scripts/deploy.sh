#!/bin/bash

# 경로 설정
ROOT_PATH="/home/ec2-user/cicd"
DOCKERFILE_PATH="$ROOT_PATH/Dockerfile"
DOCKER_COMPOSE_PATH="$ROOT_PATH/docker-compose.yml"

# 이미지 이름과 태그 설정
IMAGE_NAME="application"
IMAGE_TAG="latest"

# 로그 파일 설정
START_LOG="$ROOT_PATH/start.log"
BUILD_LOG="$ROOT_PATH/build.log"

touch $START_LOG $BUILD_LOG

# Gradle 빌드 실행
echo "[$(date +%c)] Gradle 빌드 시작" >> $START_LOG
cd $ROOT_PATH || exit

# gradlew에 실행 권한 부여
chmod +x ./gradlew

./gradlew build -x test >> $BUILD_LOG 2>&1

if [ $? -eq 0 ]; then
    echo "[$(date +%c)] Gradle 빌드 성공" >> $START_LOG

    # Docker 이미지 빌드
    echo "[$(date +%c)] Docker 이미지 빌드 시작" >> $START_LOG
    docker build -t $IMAGE_NAME:$IMAGE_TAG -f $DOCKERFILE_PATH $ROOT_PATH >> $BUILD_LOG 2>&1

    if [ $? -eq 0 ]; then
        echo "[$(date +%c)] Docker 이미지 빌드 성공: $IMAGE_NAME:$IMAGE_TAG" >> $START_LOG

        # Docker Compose를 사용하여 애플리케이션 시작
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
