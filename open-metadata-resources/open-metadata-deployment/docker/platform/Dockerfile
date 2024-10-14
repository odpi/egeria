# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

# Usage: docker run -d -p 9443:9443 odpi/egeria-platform
# 
# All dynamic data is stored under /deployments/data, so map a volume or k8s pvc to this 
# to persist egeria configuration, local graph repo etc ie
# docker run -v source=egeria-data,target=/deployments/data odpi/egeria:latest 
#
# See readme in source tree or on docker hub for more info

# Based on RedHat UBI image.
# https://catalog.redhat.com/software/containers/ubi9/openjdk-17-runtime/61ee7d45384a3eb331996bee

# This can be switched to the runtime image for a smaller footprint image
# However the JDK debugging tools will be lost, which are helpful in monitoring & undersanding performance/resource issues
#FROM registry.access.redhat.com/ubi9/openjdk-17-runtime
FROM registry.access.redhat.com/ubi9/openjdk-17

ARG version=5.2-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown
ARG CONTAINER=unknown

ENV version=${version}

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor="ODPi" \
      org.opencontainers.image.title="Egeria OMAG Server Platform" \
      org.opencontainers.image.description="Common image for Egeria's OMAG Server Platform runtime. Based on RedHat UBI 8 openjdk-17 image." \
      org.opencontainers.image.url="https://egeria-project.org/" \
      org.opencontainers.image.source="$VCS_ORIGIN" \
      org.opencontainers.image.authors="Egeria project community" \
      org.opencontainers.image.revision="$VCS_REF" \
      org.opencontainers.image.licenses="Apache-2.0" \
      org.opencontainers.image.created="$BUILD_TIME" \
      org.opencontainers.image.version="$version" \
      org.opencontainers.image.documentation="https://github.com/odpi/egeria/tree/main/open-metadata-resources/open-metadata-deployment/docker/platform" \
      org.opencontainers.image.ext.vcs-date="$VCS_DATE" \
      org.opencontainers.image.ext.docker.cmd="docker run -d -p 9443:9443 odpi/egeria-platform" \
      org.opencontainers.image.ext.docker.cmd.devel="docker run -d -p 9443:9443 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria-platform" \
      org.opencontainers.image.ext.docker.debug="docker exec -it $CONTAINER /bin/sh" \
      org.opencontainers.image.ext.docker.params="JAVA_DEBUG=set to true to enable JVM debugging"

# Copy the files to package into the container.  The chmod sets up the needed file permissions for the
# libraries and the data directory.
COPY --chmod=777 assembly/platform /deployments

# Expose port 9443 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 9443 5005

# By default, we run the OMAG Server Platform
ENV JAVA_APP_JAR=omag-server-platform-${version}.jar

# These environment variables are a workaround for an incompatibility between the macOS encyryption library and XTDB's
# expectionations.
ENV XTDB_DISABLE_LIBCRYPTO=True
ENV XTDB_ENABLE_BYTEUTILS_SHA1=True

# This is used with regular class loader, ie any client code etc
# ENV JAVA_LIBDIR=/deployments/lib

# This is used when running a spring app such as the OMAG Server Platform (default)
# Extend this accordingly via the environment, or within a new Dockerfile as needed
ENV LOADER_PATH=/deployments/lib,/deployments/extra

# Joloka will be removed shortly. For now disable to avoid errors
# ENV AB_JOLOKIA_OFF=true

# See issue 3740 - we leave the metaspace size as unlimited - jvm default
ENV JAVA_OPTS_APPEND="-XX:MaxMetaspaceSize=1g"
