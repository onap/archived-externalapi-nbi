FROM openjdk:8-jdk-alpine

ARG PKG_FILENAME=nbi-rest-services-1.0.0-SNAPSHOT.jar

ADD target/$PKG_FILENAME app.jar
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar"
ENTRYPOINT exec java $JAVA_OPTS  /app.jar