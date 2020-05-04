# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM maven:3.6.3-openjdk-8
ARG DOWNLOAD_SERVER="https://archive.apache.org/dist"
ARG RANGER_VERSION=1.2.0
ARG SOLR_VERSION=7.7.3

ENV RANGER_URL="${DOWNLOAD_SERVER}/ranger/${RANGER_VERSION}/apache-ranger-${RANGER_VERSION}.tar.gz" \
    RANGER_KEYS="${DOWNLOAD_SERVER}/ranger/KEYS"

WORKDIR /opt

# Pull down Apache Atlas and build it into /root/atlas-bin.
RUN set -e; \
    apt-get update && \
    apt-get install -y build-essential && \
    wget -nv "$RANGER_URL" -O "apache-ranger-$RANGER_VERSION.tar.gz" && \
    wget -nv "$RANGER_URL.asc" -O "apache-ranger-$RANGER_VERSION.tar.gz.asc" && \
    wget -nv "$RANGER_KEYS" -O "ranger-KEYS" && \
    gpg --import ranger-KEYS && \
    gpg --verify apache-ranger-$RANGER_VERSION.tar.gz.asc apache-ranger-$RANGER_VERSION.tar.gz && \
    tar zxf apache-ranger-$RANGER_VERSION.tar.gz

WORKDIR /opt/apache-ranger-$RANGER_VERSION
COPY dist/settings.xml /root/.m2/settings.xml

# Build everything
RUN mvn -Pall -DskipTests=true clean compile package install assembly:assembly

# Package the admin portion
RUN mkdir -p /opt/ranger-admin-bin && \
    tar zxf /opt/apache-ranger-$RANGER_VERSION/target/ranger-*-admin.tar.gz --strip-components 1 -C /opt/ranger-admin-bin

COPY dist/ranger-admin-start.sh /opt/ranger-admin-bin/ranger-admin-start.sh
RUN chmod 755 /opt/ranger-admin-bin/ranger-admin-start.sh

# Configure and install Solr locally (for now)
WORKDIR /opt/ranger-admin-bin/contrib/solr_for_audit_setup
RUN sed -i 's|SOLR_INSTALL=false|SOLR_INSTALL=true|' install.properties && \
    sed -i "s|SOLR_DOWNLOAD_URL=|SOLR_DOWNLOAD_URL=http://www.mirrorservice.org/sites/ftp.apache.org/lucene/solr/$SOLR_VERSION/solr-$SOLR_VERSION.tgz|" install.properties && \
    ./setup.sh && \
    sed -i 's|^SOLR_JAVA_HOME=|#SOLR_JAVA_HOME=|g' /opt/solr/ranger_audit_server/scripts/solr.in.sh
