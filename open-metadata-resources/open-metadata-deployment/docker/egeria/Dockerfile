# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM maven:3.6.2-slim AS build
ARG version
ARG repo

# Default to latest snapshot from official odpi repository
ENV version ${version:-"1.1-SNAPSHOT"}
ENV repo ${repo:-"https://odpi.jfrog.io/odpi/egeria-snapshot/"}

# WORKDIR /opt

# Force Docker layer cache invalidation if there have been changes to the Egeria repo
ADD https://nexus.odpi.org/content/groups/public/org/odpi/egeria/egeria/${version}/ index.html

WORKDIR /opt/egeria
RUN mkdir /opt/egeria/binaries

RUN mvn org.apache.maven.plugins:maven-dependency-plugin:2.5.1:get -DremoteRepositories=$repo \
    -Dartifact=org.odpi.egeria:open-metadata-assemblies:${version}:jar -Ddest=egeria.jar && \
    jar -xf egeria.jar egeria-${version}-distribution.tar.gz && \
    gunzip -c egeria-${version}-distribution.tar.gz | tar -xf -  \
      egeria-omag-${version}/user-interface/access-services-user-interface-${version}.jar \
      egeria-omag-${version}/server/server-chassis-spring-${version}.jar && \
    mv egeria-omag-${version}/user-interface/access-services-user-interface-${version}.jar /opt/egeria/binaries/ && \
    mv egeria-omag-${version}/server/server-chassis-spring-${version}.jar /opt/egeria/binaries/ 
    
FROM openjdk:8-jre-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "egeria"
LABEL org.label-schema.description = "Common image for core ODPi Egeria runtimes: OMAG Server Platform (chassis) and UI."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/egeria"
LABEL org.label-schema.docker.cmd = "docker run -d -p 8080:8080 odpi/egeria"
LABEL org.label-schema.docker.cmd.devel = "docker run -d -p 8080:8080 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "JAVA_DEBUG=set to true to enable JVM debugging"

RUN apk --no-cache add bash shadow && \
    apk --no-cache update && \
    apk --no-cache upgrade && \
    groupadd -r egeria -g 8080 && \
    useradd --no-log-init -r -g egeria -u 8080 -d /opt/egeria egeria

COPY --from=build --chown=egeria:egeria /opt/egeria/binaries/server-chassis-spring-*.jar /opt/egeria/server-chassis-spring.jar
COPY --from=build --chown=egeria:egeria /opt/egeria/binaries/access-services-user-interface-*.jar /opt/egeria/access-services-user-interface.jar
COPY --chown=egeria:egeria dist/entrypoint.sh /entrypoint.sh

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005

WORKDIR /opt/egeria
USER egeria:egeria

CMD java -jar /opt/egeria/server-chassis-spring.jar
ENTRYPOINT ["/entrypoint.sh"]
