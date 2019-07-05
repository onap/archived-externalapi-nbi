#
#     Copyright (c) 2018 Orange
#
#     Licensed under the Apache License, Version 2.0 (the "License");
#     you may not use this file except in compliance with the License.
#     You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
#     Unless required by applicable law or agreed to in writing, software
#     distributed under the License is distributed on an "AS IS" BASIS,
#     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#     See the License for the specific language governing permissions and
#     limitations under the License.
#

FROM openjdk:11-jre-slim

ARG SERVER_PORT
ARG PKG_FILENAME=nbi-rest-services-4.0.1-SNAPSHOT.jar
ADD target/$PKG_FILENAME app.jar

RUN addgroup appgroup && useradd -ms /bin/bash appuser && adduser appuser appgroup && mkdir temptoscafile && chown appuser:appgroup temptoscafile/

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

USER appuser:appgroup

ENV SERVER_PORT=${SERVER_PORT:-8080}
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

EXPOSE $SERVER_PORT
ENTRYPOINT java -XX:+UseContainerSupport $JAVA_OPTS -jar /app.jar
