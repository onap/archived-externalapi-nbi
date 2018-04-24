FROM openjdk:8-jre-alpine

ARG SERVER_PORT
ARG PKG_FILENAME=nbi-rest-services-1.0.0-SNAPSHOT.jar
ADD target/$PKG_FILENAME app.jar

COPY src/main/resources/certificate /certs
ARG CERT_PASS=changeit
RUN for cert in $(ls -d /certs/*); do \
        echo "adding $cert to java keystore..."; \
        keytool -import \
                -file "$cert" \
                -storepass "${CERT_PASS}" \
                -keystore $JAVA_HOME/lib/security/cacerts \
                -alias "$(basename $cert)" \
                --noprompt; \
    done

ENV SERVER_PORT=${SERVER_PORT:-8080}
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -jar"

EXPOSE $SERVER_PORT
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
