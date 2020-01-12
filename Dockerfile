FROM openjdk:8u171-alpine
USER 1001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-Xms256m","-Xmx256m","-jar","/app.jar"]
COPY build/libs/nifi-q-monitor-*.jar app.jar
COPY src/main/resources/static /opt/site
