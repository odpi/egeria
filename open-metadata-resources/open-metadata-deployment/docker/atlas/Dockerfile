# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.04

LABEL org.label-schema.name = "AtlasForEgeriaVDC"
LABEL org.label-schema.description="Atlas service to support Egeria VDC demos (including ATLAS-1773 patch Oct 2018)"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/atlas"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget

# Install Java.
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install Maven.

ENV MAVEN_HOME /usr/share/maven

# Add Java and Maven to the path.
ENV PATH /usr/java/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root

# Pull down Atlas and build it into /root/atlas-bin.
RUN git clone http://git.apache.org/atlas.git -b master

#Apply patch
RUN wget https://issues.apache.org/jira/secure/attachment/12952687/ATLAS-1773-2018-12-21.patch
RUN wget https://issues.apache.org/jira/secure/attachment/12952688/ATLAS-2810-2018-12-21.patch
RUN wget https://issues.apache.org/jira/secure/attachment/12952689/ATLAS-2939-2018-12-21.patch
RUN wget https://issues.apache.org/jira/secure/attachment/12952690/ATLAS-2985-2018-12-21.patch
WORKDIR atlas
# Stick with mid dec build due to regression impacting fixes
RUN   git config --global user.email "you@example.com"
RUN   git config --global user.name "Your Name"
RUN git am < ../ATLAS-2810-2018-12-21.patch
RUN git am < ../ATLAS-2939-2018-12-21.patch
RUN git am < ../ATLAS-2985-2018-12-21.patch
RUN git am < ../ATLAS-1773-2018-12-21.patch

# Add reference to odpi repository for snapshots
COPY dist/settings.xml /root/.m2/settings.xml
WORKDIR /root

# need more java heap
ENV JAVA_TOOL_OPTIONS="-Xmx1024m"

# Remove -DskipTests if unit tests are to be included
RUN mvn clean install -DskipTests -Pdist,embedded-hbase-solr -f ./atlas/pom.xml
RUN mkdir -p atlas-bin
RUN tar xzf /root/atlas/distro/target/*bin.tar.gz --strip-components 1 -C /root/atlas-bin

# Set env variables, add it to the path, and start Atlas.
ENV MANAGE_LOCAL_SOLR true
ENV MANAGE_LOCAL_HBASE true
ENV PATH /root/atlas-bin/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

EXPOSE 21000

CMD ["/bin/bash", "-c", "/root/atlas-bin/bin/atlas_start.py; tail -fF /root/atlas-bin/logs/application.log"]
