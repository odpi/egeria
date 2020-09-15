# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

# Based on RedHat UBI image.
# https://catalog.redhat.com/software/containers/ubi8/openjdk-11/5dd6a4b45a13461646f677f4?container-tabs=gti&gti-tabs=unauthenticated
FROM registry.access.redhat.com/ubi8/openjdk-11
ARG version=2.3-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

ENV version ${version}

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor = "ODPi" \
      org.opencontainers.image.title = "Egeria" \
      org.opencontainers.image.description = "Common image for core ODPi Egeria runtime. Based on RedHat UBI 8 openjdk-11 image" \
      org.opencontainers.image.url = "https://egeria.odpi.org/" \
      org.opencontainers.image.source = "$VCS_ORIGIN" \
      org.opencontainers.image.authors = "ODPi Egeria" \
      org.opencontainers.image.revision = "$VCS_REF" \
      org.opencontainers.image.licenses = "Apache-2.0" \
      org.opencontainers.image.created = "$BUILD_TIME" \
      org.opencontainers.image.version = "$version" \
      org.opencontainers.image.documentation = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria/" \
      org.opencontainers.image.ext.vcs-date = "$VCS_DATE" \
      org.opencontainers.image.ext.docker.cmd = "docker run -d -p 9443:9443 odpi/egeria" \
      org.opencontainers.image.ext.docker.cmd.devel = "docker run -d -p 9443:9443 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria" \
      org.opencontainers.image.ext.docker.debug = "docker exec -it $CONTAINER /bin/sh" \
      org.opencontainers.image.ext.docker.params = "JAVA_DEBUG=set to true to enable JVM debugging"

# Copy egeria distribution
COPY target/assembly /deployments

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 9443 5005

# By default, we run the server chassis
ENV JAVA_APP_JAR=server/server-chassis-spring-${version}.jar
ENV JAVA_LIBDIR=${JAVA_APP_DIR}/server/lib
ENV LOADER_PATH=${LOADER_PATH}

# Joloka will be removed shortly. For now disable to avoid errors
ENV AB_JOLOKIA_OFF=true
