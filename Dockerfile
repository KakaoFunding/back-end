FROM openjdk:17-jdk
WORKDIR /app
ADD build/libs/*SNAPSHOT.jar app.jar
ADD src/main/resources/application-prod.yml /app/config/
RUN bash -c 'touch /app.jar'
ENV USE_PROFILE=prod
ENTRYPOINT ["java", "-Dspring.config.location=config/", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "app.jar"]
