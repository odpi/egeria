# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM maven:3.6.3-openjdk-8
ARG DOWNLOAD_SERVER="https://archive.apache.org/dist"
ARG ATLAS_VERSION=2.0.0

ENV ATLAS_URL="${DOWNLOAD_SERVER}/atlas/${ATLAS_VERSION}/apache-atlas-${ATLAS_VERSION}-sources.tar.gz" \
    ATLAS_KEYS="${DOWNLOAD_SERVER}/atlas/KEYS" \
    JAVA_TOOL_OPTIONS="-Xmx1024m"

WORKDIR /opt

# Pull down Apache Atlas and build it into /root/atlas-bin.
RUN set -e; \
  wget -nv "$ATLAS_URL" -O "apache-atlas-$ATLAS_VERSION.tar.gz" && \
  wget -nv "$ATLAS_URL.asc" -O "apache-atlas-$ATLAS_VERSION.tar.gz.asc" && \
  wget -nv "$ATLAS_KEYS" -O "atlas-KEYS" && \
  gpg --import atlas-KEYS && \
  gpg --verify apache-atlas-$ATLAS_VERSION.tar.gz.asc apache-atlas-$ATLAS_VERSION.tar.gz && \
  tar zxf apache-atlas-$ATLAS_VERSION.tar.gz

WORKDIR /opt/apache-atlas-sources-$ATLAS_VERSION
COPY dist/settings.xml /root/.m2/settings.xml

# Remove -DskipTests if unit tests are to be included
RUN mvn clean -DskipTests=true install && \
    mvn clean -DskipTests=true package -Pdist,embedded-hbase-solr && \
    mkdir -p /opt/atlas-bin && \
    tar zxf /opt/apache-atlas-sources-$ATLAS_VERSION/distro/target/*server.tar.gz --strip-components 1 -C /opt/atlas-bin
