#!/bin/bash

# shellcheck disable=SC2164
cd /home/ec2-user/cicd
docker-compose -f /home/ec2-user/cicd/docker-compose.yml down