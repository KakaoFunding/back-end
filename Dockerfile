FROM openjdk:17-jdk
ADD build/libs/*SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
ENV USE_PROFILE=prod
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/,classpath:/config/,file:./", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
