FROM openjdk:17-jdk
ADD build/libs/spring-project.jar /app.jar
RUN mkdir /config
ADD build/resources/application-prod.yml /config/application-prod.yml
RUN bash -c 'touch /app.jar'
ENV USE_PROFILE=prod
ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/config/", "-Dspring.profiles.active=${USE_PROFILE}", "/app.jar"]

