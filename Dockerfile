FROM openjdk:17-jdk
ADD build/libs/*SNAPSHOT.jar /app.jar
ADD /home/ec2-user/application-prod.yml /config/application-prod.yml
RUN bash -c 'touch /app.jar'
ENV USE_PROFILE=prod
ENTRYPOINT ["java","-Dspring.config.location=/config/","-Dspring.profiles.active=${USE_PROFILE}","-jar", "/app.jar"]
