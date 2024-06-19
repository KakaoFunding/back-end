#!/bin/bash

if [ -f /home/ec2-user/cicd/backup/application-prod.yml ]; then
    cp /home/ec2-user/cicd/backup/application-prod.yml /home/ec2-user/cicd/src/main/resources/application-prod.yml
fi
