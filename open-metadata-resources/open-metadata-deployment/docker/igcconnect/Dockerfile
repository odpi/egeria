# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.
#
# Initial, simple, Egeria install - using all defaults. In-memory repository only (no persistence)
# Built entirely from source, and within the container environment
# Though this adds time to the build of the dockerfile, it ensures consistency & currency
# The build will take a while, as maven dependencies need to be loaded afresh into ~/.m2
FROM ubuntu:18.04

# Image metadata following http://label-schema.org/rc1/
LABEL org.label-schema.name = "IGCConnectForEgeriaVDC"
LABEL org.label-schema.description="IGC proxy/connector for Egeria"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/igcconnect"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git,java sdk (openjdk) & maven -- needed to built Egeria
RUN apt-get update && apt-get install -y openjdk-8-jdk maven git

# Setup some environment variables for later
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
#ENV MAVEN_HOME /usr/share/maven

# Add Java 8 (egeria will not build with Java 10 - default in Ubuntu 18.04) and Maven to the path.
ENV PATH /usr/lib/jvm/java-8-openjdk-amd64/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory - root home
WORKDIR /root
COPY dist/pom.xml .
RUN mvn -DremoteRepositories=https://nexus.odpi.org/content/repositories/snapshots/ org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -Dartifact=org.odpi.egeria:igc-repository-proxy:1.1-SNAPSHOT
RUN mvn install org.apache.maven.plugins:maven-dependency-plugin:3.1.1:copy -Dartifact=org.odpi.egeria:igc-repository-proxy:1.1-SNAPSHOT -DoutputDirectory=.

# now launch the igc repository proxy
CMD [ "java", "-jar", "./igc-repository-proxy-1.1-SNAPSHOT.jar" ]

