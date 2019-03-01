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

FROM openjdk:8-jre-alpine
COPY --from=build /root/egeria/open-metadata-distribution/open-metadata-assemblies/target/egeria-*-omag-server/servers/omag-server-*/server-chassis-spring-*.jar /server-chassis-spring.jar
RUN apk add bash
COPY dist/entrypoint.sh /entrypoint.sh
# Expose port 8080 (default) for client access, and allow for 5005 being used for remote java debug
EXPOSE 8080 5005
CMD java -jar /server-chassis-spring.jar
ENTRYPOINT ["/entrypoint.sh"]
