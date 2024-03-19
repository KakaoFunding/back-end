#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/kakao-funding
cd $REPOSITORY

APP_NAME=kakao-funding
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -fl $APP_NAME | grep java | awk '{print $1}')

if [ -z "$CURRENT_PID" ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH "
nohup java -jar $JAR_PATH > kakao-funding.log 2>&1 < /dev/null &
