FROM openjdk:17-jdk
ADD build/libs/*SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar","-Dsecurity.token.access.expire-time=10000", "-Dspring.profiles.active=prod", "/app.jar"]

