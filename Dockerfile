FROM openjdk:8-jre-alpine

ARG SERVER_PORT
ARG PKG_FILENAME=nbi-rest-services-1.0.0-SNAPSHOT.jar
ADD target/$PKG_FILENAME app.jar

ENV SERVER_PORT=${SERVER_PORT:-8080}
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -jar"

EXPOSE $SERVER_PORT
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
