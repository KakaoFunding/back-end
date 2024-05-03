#!/bin/bash

for PORT in 8081 8082 8083; do
    CONTAINER_ID=$(docker ps --filter "publish=$PORT" --format "{{.ID}}")
    if [ ! -z "$CONTAINER_ID" ]; then
        echo "Stopping container on port $PORT..."
        docker stop $CONTAINER_ID
    else
        echo "No container is running on port $PORT."
    fi
done
