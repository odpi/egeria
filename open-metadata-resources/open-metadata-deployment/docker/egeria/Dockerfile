# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.
#
# Initial, simple, Egeria install - using all defaults. In-memory repository only (no persistence)
# Built entirely from source, and within the container environment
# Though this adds time to the build of the dockerfile, it ensures consistency & currency
# The build will take a while, as maven dependencies need to be loaded afresh into ~/.m2
FROM ubuntu:18.04

# Image metadata following http://label-schema.org/rc1/
LABEL org.label-schema.name = "EgeriaForEgeriaVDC"
LABEL org.label-schema.description="Egeria service to support Egeria VDC demos"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/egeria"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git,java sdk (openjdk) & maven -- needed to built Egeria
RUN apt-get update && apt-get install -y openjdk-8-jdk maven git git-extras

# Setup some environment variables for later
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
#ENV MAVEN_HOME /usr/share/maven

# Add Java 8 (egeria will not build with Java 10 - default in Ubuntu 18.04) and Maven to the path.
ENV PATH /usr/lib/jvm/java-8-openjdk-amd64/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory - root home
WORKDIR /root

ARG              branch
ENV              branch                  ${branch:-master}

ARG		 pr

# Pull down Egeria and build it
RUN git clone http://github.com/odpi/egeria.git -b ${branch}

WORKDIR /root/egeria
# Add Pull request if specified (requires git-extras package)
RUN if [ ! -z "$pr" ] ; then git pr $pr ; fi

RUN mvn clean install -DskipTests

# Launch the omag server in it's built & packaged location
WORKDIR /root/egeria/open-metadata-distribution/open-metadata-assemblies/target/egeria-0.3-SNAPSHOT-omag-server/servers/omag-server-0.3-SNAPSHOT

# Expose port 8080 (default) for client access
EXPOSE 8080

# The container will run the server chassis on startup -
CMD ["/bin/bash", "-c", "java -jar server-chassis-spring-0.3-SNAPSHOT.jar"]
