FROM openjdk:17-jdk
ADD build/libs/*SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
ENV USE_PROFILE=prod
ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/config/", "-Dspring.profiles.active=${USE_PROFILE}", "/app.jar"]

