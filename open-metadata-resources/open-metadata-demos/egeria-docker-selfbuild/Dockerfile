# SPDX-License-Identifier: Apache-2.0
#
# Initial, simple, Egeria install - using all defaults. In-memory repository only (no persistence)
# Built entirely from source, and within the container environment
# Though this adds time to the build of the dockerfile, it ensures consistency & currency
# The build will take a while, as maven dependencies need to be loaded afresh into ~/.m2
FROM ubuntu:18.04

# Install Git,java sdk (openjdk) & maven -- needed to built Egeria
RUN apt-get update && apt-get install -y openjdk-8-jdk maven git

# Setup some environment variables for later
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
#ENV MAVEN_HOME /usr/share/maven

# Add Java 8 (egeria will not build with Java 10 - default in Ubuntu 18.04) and Maven to the path.
ENV PATH /usr/lib/jvm/java-8-openjdk-amd64/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory - root home
WORKDIR /root

# Pull down Egeria and build it
RUN git clone http://github.com/odpi/egeria.git -b master

# Remove -DskipTests if unit tests are to be included - build the code...
WORKDIR /root/egeria
RUN mvn clean install -DskipTests

# Launch the omag server in it's built & packaged location
WORKDIR /root/egeria/open-metadata-distribution/open-metadata-assemblies/target/egeria-0.2-SNAPSHOT-omag-server/servers/omag-server-0.2-SNAPSHOT

# Expose port 8080 (default) for client access
EXPOSE 8080

# The container will run the server chassis on startup -
CMD ["/bin/bash", "-c", "java -jar server-chassis-spring-0.2-SNAPSHOT.jar"]
