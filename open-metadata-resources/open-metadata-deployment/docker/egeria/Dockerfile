# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM openjdk:8-jre-alpine
ARG version=1.8-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

ENV version ${version}

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor = "ODPi" \
      org.opencontainers.image.title = "Egeria" \
      org.opencontainers.image.description = "Common image for core ODPi Egeria runtime." \
      org.opencontainers.image.url = "https://egeria.odpi.org/" \
      org.opencontainers.image.source = "$VCS_ORIGIN" \
      org.opencontainers.image.authors = "ODPi Egeria" \
      org.opencontainers.image.revision = "$VCS_REF" \
      org.opencontainers.image.licenses = "Apache-2.0" \
      org.opencontainers.image.created = "$BUILD_TIME" \
      org.opencontainers.image.version = "$version" \
      org.opencontainers.image.documentation = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria/" \
      org.opencontainers.image.ext.vcs-date = "$VCS_DATE" \
      org.opencontainers.image.ext.docker.cmd = "docker run -d -p 8080:8080 odpi/egeria" \
      org.opencontainers.image.ext.docker.cmd.devel = "docker run -d -p 8080:8080 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria" \
      org.opencontainers.image.ext.docker.debug = "docker exec -it $CONTAINER /bin/sh" \
      org.opencontainers.image.ext.docker.params = "JAVA_DEBUG=set to true to enable JVM debugging"

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

# Require use of numeric userids to meet some container security checks
USER 8080:8080

# Spring loader path for connectors etc
ENV LOADER_PATH=/opt/egeria/server/lib

# Launch server chassis by default
CMD java -jar /opt/egeria/server/server-chassis-spring-${version}.jar

ENTRYPOINT ["/entrypoint.sh"]
