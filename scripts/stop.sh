#!/bin/bash

echo "Stopping all containers managed by docker-compose..."
docker-compose down

echo "All containers stopped and cleaned up successfully."
