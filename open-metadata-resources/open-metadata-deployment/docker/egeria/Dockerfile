# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM maven:3.6.0-jdk-8 AS build
RUN apt-get update && apt-get install git-extras
ARG branch
ARG pr
ENV branch ${branch:-master}
WORKDIR /root/
RUN git clone http://github.com/odpi/egeria.git -b ${branch}
WORKDIR /root/egeria/
# Add Pull request if specified (requires git-extras package)
RUN if [ ! -z "$pr" ] ; then git pr $pr ; fi
RUN mvn clean install -DskipTests
RUN git log > git.log && echo ${branch} > git.branch && git remote -v >> git.remote

FROM openjdk:8-jre-alpine

RUN apk --no-cache add bash shadow && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r egeria -g 1000 && useradd --no-log-init -r -g egeria -u 1000 -d /opt/egeria egeria

COPY --from=build --chown=egeria:egeria /root/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/target/server-chassis-spring-*.jar /opt/egeria/server-chassis-spring.jar
COPY --from=build --chown=egeria:egeria /root/egeria/open-metadata-implementation/user-interfaces/access-services-user-interface/target/access-services-user-interface-*.jar /opt/egeria/access-services-user-interface.jar
COPY --from=build --chown=egeria:egeria /root/egeria/git.* /opt/egeria/details/
COPY --chown=egeria:egeria dist/entrypoint.sh /entrypoint.sh

# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005

WORKDIR /opt/egeria
USER egeria:egeria

CMD java -jar /opt/egeria/server-chassis-spring.jar
ENTRYPOINT ["/entrypoint.sh"]
