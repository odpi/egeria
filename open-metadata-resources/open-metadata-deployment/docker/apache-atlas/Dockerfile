# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.10 AS build

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget inetutils-ping dnsutils gzip tar gpg

# Install Java.
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install Maven.
ENV MAVEN_HOME /usr/share/maven

# Add Java and Maven to the path.
ENV PATH /usr/java/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root

# Pull down Atlas and build it into /root/atlas-bin.
RUN wget http://www.mirrorservice.org/sites/ftp.apache.org/atlas/1.1.0/apache-atlas-1.1.0-sources.tar.gz
RUN wget https://www.apache.org/dist/atlas/KEYS
RUN wget https://www.apache.org/dist/atlas/1.1.0/apache-atlas-1.1.0-sources.tar.gz.asc

RUN gpg --import KEYS
RUN gpg --verify apache-atlas-1.1.0-sources.tar.gz.asc apache-atlas-1.1.0-sources.tar.gz

RUN tar zxf apache-atlas-1.1.0-sources.tar.gz

WORKDIR /root/apache-atlas-sources-1.1.0

# need more java heap
ENV JAVA_TOOL_OPTIONS="-Xmx1024m"

# Remove -DskipTests if unit tests are to be included
RUN mvn clean install -DskipTests -Pdist,embedded-hbase-solr -DskipEnunciate -f ./pom.xml
RUN mkdir -p /root/atlas-bin
RUN tar xzf /root/apache-atlas-sources-1.1.0/distro/target/*bin.tar.gz --strip-components 1 -C /root/atlas-bin

FROM openjdk:8-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "apache-atlas"
LABEL org.label-schema.description = "Apache Atlas image to support ODPi Egeria demonstrations."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/atlas"
LABEL org.label-schema.docker.cmd = "docker run -d -p 21000:21000 odpi/apache-atlas"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"

RUN apk --no-cache add python bash && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r atlas -g 1000 && useradd --no-log-init -r -g atlas -u 1000 -d /opt/apache/atlas atlas

COPY --from=build --chown=atlas:atlas /root/atlas-bin/ /opt/apache/atlas/

USER atlas:atlas

WORKDIR /opt/apache/atlas/conf
RUN sed -i "s|^atlas.graph.storage.lock.wait-time=10000|atlas.graph.storage.lock.wait-time=100|g" atlas-application.properties
RUN echo "atlas.notification.relationships.enabled=true" >> atlas-application.properties

# Set env variables, add it to the path, and start Atlas.
ENV JAVA_TOOL_OPTIONS="-Xmx1024m"
ENV MANAGE_LOCAL_SOLR true
ENV MANAGE_LOCAL_HBASE true
ENV PATH /opt/apache/atlas/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

EXPOSE 21000
ENTRYPOINT ["/bin/bash", "-c", "/opt/apache/atlas/bin/atlas_start.py; tail -fF /opt/apache/atlas/logs/application.log"]
