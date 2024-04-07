FROM openjdk:17-jdk
EXPOSE 8080

ADD CNTS-backend-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
