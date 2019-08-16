# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM maven:3.6.0-jdk-8 AS build
ARG branch
ARG pr

ENV branch ${branch:-master}

WORKDIR /opt

# Force Docker layer cache invalidation if there have been changes to the Egeria branch
ADD https://api.github.com/repos/odpi/egeria/git/refs/heads/$branch version.json

RUN set -e; \
    apt-get update && \
    apt-get install -y git-extras && \
    git clone https://github.com/odpi/egeria.git -b ${branch}

WORKDIR /opt/egeria

# Add Pull request if specified (requires git-extras package), build, and then place distributable files
RUN if [ ! -z "$pr" ] ; then git pr $pr ; fi && \
    mvn clean install -DskipTests && \
    git log > git.log && echo ${branch} > git.branch && \
    git remote -v >> git.remote && \
    mkdir /opt/egeria/binaries && \
    mv /opt/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/target/server-chassis-spring-[1-9]\.[0-9]\-SNAPSHOT\.jar /opt/egeria/binaries/ && \
    mv /opt/egeria/open-metadata-implementation/user-interfaces/access-services-user-interface/target/access-services-user-interface-[1-9]\.[0-9]\-SNAPSHOT\.jar /opt/egeria/binaries/

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
COPY --from=build --chown=egeria:egeria /opt/egeria/git.* /opt/egeria/details/
COPY --chown=egeria:egeria dist/entrypoint.sh /entrypoint.sh

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005

WORKDIR /opt/egeria
USER egeria:egeria

CMD java -jar /opt/egeria/server-chassis-spring.jar
ENTRYPOINT ["/entrypoint.sh"]
