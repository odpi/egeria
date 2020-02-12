# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM openjdk:8-jre-alpine
ARG version
ARG repo

# Default to latest snapshot from official odpi repository
ENV version ${version:-"1.4"}
ENV repo ${repo:-"https://odpi.jfrog.io/odpi/egeria-snapshot/"}

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
    useradd --no-log-init -r -g egeria -u 8080 -m -d /opt/egeria egeria

COPY --chown=egeria:egeria dist/entrypoint.sh /entrypoint.sh

ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/server-chassis-spring/${version}/maven-metadata.xml /opt/egeria/server-chassis.xml
ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/ui-chassis-spring/${version}/maven-metadata.xml /opt/egeria/user-chassis-spring.xml
ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/open-lineage-janus-connector/${version}/maven-metadata.xml /opt/egeria/lib/janus-connector-jar-with-dependencies.xml
ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/server-chassis-spring/${version}/server-chassis-spring-${version}.jar /opt/egeria/server-chassis-spring.jar
ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/ui-chassis-spring/${version}/ui-chassis-spring-${version}.jar /opt/egeria/ui-chassis-spring.jar
ADD --chown=egeria:egeria ${repo}/org/odpi/egeria/open-lineage-janus-connector/${version}/open-lineage-janus-connector-${version}-jar-with-dependencies.jar /opt/egeria/lib/janus-connector-jar-with-dependencies.jar

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005

WORKDIR /opt/egeria
USER egeria:egeria

CMD java -jar /opt/egeria/server-chassis-spring.jar
ENTRYPOINT ["/entrypoint.sh"]
