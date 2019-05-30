# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.10 AS build

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget inetutils-ping dnsutils gzip tar

# Install Java.
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install Maven.
ENV MAVEN_HOME /usr/share/maven

# Add Java and Maven to the path.
ENV PATH /usr/java/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root

# Pull down Atlas and build it into /root/atlas-bin.
RUN git clone https://github.com/grahamwallis/atlas -b master

WORKDIR /root/atlas

# Appy any patches - temporary as per #922
COPY dist/0001-EGERIA-github-922-upgrade-kafka-version.patch /root/0001-EGERIA-github-922-upgrade-kafka-version.patch
COPY dist/0002-EGERIA-github-922-upgrade-findbugs.patch /root/0002-EGERIA-github-922-upgrade-findbugs.patch
COPY dist/0003-Fixup-Admin-services-responses-void-and-success.patch /root/0003-Fixup-Admin-services-responses-void-and-success.patch
COPY dist/0004-ignore-map-string-object-and-allow-creation-of-conne.patch /root/0004-ignore-map-string-object-and-allow-creation-of-conne.patch
COPY dist/0005-Correction-to-type-object.patch /root/0005-Correction-to-type-object.patch
RUN git config --global user.email "you@example.com"
RUN git config --global user.name "Your Name"
RUN git am < /root/0001-EGERIA-github-922-upgrade-kafka-version.patch
RUN git am < /root/0002-EGERIA-github-922-upgrade-findbugs.patch
RUN git am < /root/0003-Fixup-Admin-services-responses-void-and-success.patch
RUN git am < /root/0004-ignore-map-string-object-and-allow-creation-of-conne.patch
RUN git am < /root/0005-Correction-to-type-object.patch
# Add reference to odpi repository for snapshots
COPY dist/settings.xml /root/.m2/settings.xml
WORKDIR /root

# need more java heap
ENV JAVA_TOOL_OPTIONS="-Xmx1024m"

# Remove -DskipTests if unit tests are to be included
RUN mvn clean install -DskipTests -Pdist,embedded-hbase-solr -DskipEnunciate -f ./atlas/pom.xml
RUN mkdir -p atlas-bin
RUN tar xzf /root/atlas/distro/target/*bin.tar.gz --strip-components 1 -C /root/atlas-bin

WORKDIR /root/atlas
RUN git log > git.log && echo ${branch} > git.branch && git remote -v >> git.remote

FROM openjdk:8-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "apache-atlas-fork"
LABEL org.label-schema.description = "Apache Atlas image with an embedded ODPi Egeria OMRS Server."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/atlas"
LABEL org.label-schema.docker.cmd = "docker run -d -p 21000:21000 odpi/apache-atlas-fork"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"

RUN apk --no-cache add python bash && apk --no-cache update && apk --no-cache upgrade
COPY --from=build /root/atlas-bin/ /root/atlas-bin/
COPY --from=build /root/atlas/git.* /
WORKDIR /root/atlas-bin/conf
RUN sed -i "s|^atlas.graph.storage.lock.wait-time=10000|atlas.graph.storage.lock.wait-time=100|g" atlas-application.properties
RUN echo "atlas.notification.relationships.enabled=true" >> atlas-application.properties

# Set env variables, add it to the path, and start Atlas.
ENV JAVA_TOOL_OPTIONS="-Xmx1024m"
ENV MANAGE_LOCAL_SOLR true
ENV MANAGE_LOCAL_HBASE true
ENV PATH /root/atlas-bin/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

EXPOSE 21000
ENTRYPOINT ["/bin/bash", "-c", "/root/atlas-bin/bin/atlas_start.py; tail -fF /root/atlas-bin/logs/application.log"]
