FROM openjdk:8-jdk-alpine
ADD target/nbi-rest-services-1.0.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar"
ENTRYPOINT exec java $JAVA_OPTS  /app.jar