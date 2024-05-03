#!/bin/bash

cd /home/ec2-user/cicd || exit

for PORT in 8081 8082 8083; do
    CONTAINER_ID=$(docker ps --filter "publish=$PORT" --format "{{.ID}}")
    if [ ! -z "$CONTAINER_ID" ]; then
        echo "Stopping container on port $PORT..."
        docker stop $CONTAINER_ID
        docker rm $CONTAINER_ID
    else
        echo "No container is running on port $PORT."
    fi
done

NGINX_CONTAINER_ID=$(docker ps --filter "name=nginx" --format "{{.ID}}")
if [ ! -z "$NGINX_CONTAINER_ID" ]; then
    echo "Stopping and removing Nginx container..."
    docker stop $NGINX_CONTAINER_ID
    docker rm $NGINX_CONTAINER_ID
else
    echo "No Nginx container is running."
fi