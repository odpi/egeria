# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM openjdk:8-jre-alpine
ARG version=1.5-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

ENV version ${version}

LABEL org.label-schema.schema-version = "1.0" \
      org.label-schema.vendor = "ODPi" \
      org.label-schema.name = "egeria" \
      org.label-schema.description = "Common image for core ODPi Egeria runtime." \
      org.label-schema.url = "https://egeria.odpi.org/" \
      org.label-schema.vcs-url = "$VCS_ORIGIN" \
      org.label-schema.vcs-ref = "$VCS_REF" \
      org.label-schema.vcs-date = "$VCS_DATE" \
      org.label-schema.build-date = "$BUILD_TIME" \
      org.label-schema.version = "$version" \
      org.label-schema.usage = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria/" \
      org.label-schema.docker.cmd = "docker run -d -p 8080:8080 odpi/egeria" \
      org.label-schema.docker.cmd.devel = "docker run -d -p 8080:8080 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria" \
      org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh" \
      org.label-schema.docker.params = "JAVA_DEBUG=set to true to enable JVM debugging"

RUN apk --no-cache add bash shadow && \
    apk --no-cache update && \
    apk --no-cache upgrade && \
    groupadd -r egeria -g 8080 && \
    useradd --no-log-init -r -g egeria -u 8080 -m -d /opt/egeria egeria

COPY --chown=egeria:egeria dist/entrypoint.sh /entrypoint.sh

# Copy distribution
COPY --chown=egeria:egeria target/assembly /opt/egeria

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005

WORKDIR /opt/egeria
USER egeria:egeria

# Spring loader path for connectors etc
ENV LOADER_PATH=/opt/egeria/server/lib

# Launch server chassis by default
CMD java -jar /opt/egeria/server/server-chassis-spring-${version}.jar

ENTRYPOINT ["/entrypoint.sh"]
