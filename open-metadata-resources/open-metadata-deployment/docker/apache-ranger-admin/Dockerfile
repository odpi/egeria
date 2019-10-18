# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project
ARG REGISTRY=registry-1.docker.io
ARG REPO=odpi
FROM $REGISTRY/$REPO/apache-ranger:build AS build
FROM openjdk:13-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "apache-ranger-admin"
LABEL org.label-schema.description = "Apache Ranger administration server, for use as part of ODPi Egeria Virtual Data Connector demonstrations."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/apache-ranger-admin"
LABEL org.label-schema.docker.cmd = "docker run -d -p 6080:6080 -p 6182:6182 -p 6083:6083 -p 6183:6183 -e PGUSER=postgres -e PGPASSWORD=passw0rd -e PGHOST=host -e RANGER_PASSWORD=admin999 odpi/apache-ranger-admin"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "PGUSER=postgres user,PGPASSWORD=postgres password,PGHOST=postgres host,RANGER_PASSWORD=password to init for ranger admin user"

RUN apk --no-cache add python bash java-postgresql-jdbc bc shadow procps && \
    apk --no-cache update && \
    apk --no-cache upgrade && \
    groupadd -r ranger -g 6080 && \
    useradd --no-log-init -r -g ranger -u 6080 -d /opt/apache/ranger-admin ranger && \
    groupadd -r solr -g 6083 && \
    useradd --no-log-init -r -g solr -u 6083 -d /opt/solr solr && \
    mkdir -p /var/log/solr/ranger_audits && \
    chown solr:solr /var/log/solr/ranger_audits

COPY --from=build --chown=ranger:ranger /opt/ranger-admin-bin/ /opt/apache/ranger-admin/
COPY --from=build --chown=solr:solr /opt/solr/ /opt/solr/

WORKDIR /opt/apache/ranger-admin
RUN sed -i "s|^DB_FLAVOR=MYSQL|DB_FLAVOR=POSTGRES|g" install.properties && \
    sed -i "s|^SQL_CONNECTOR_JAR=/usr/share/java/mysql-connector-java.jar|SQL_CONNECTOR_JAR=/usr/share/java/postgresql-jdbc.jar|g" install.properties && \
    sed -i "s|^audit_solr_urls=|audit_solr_urls=http://localhost:6083/solr/ranger_audits|g" install.properties

EXPOSE 6080 6182 6083 6183

ENTRYPOINT ["/bin/bash", "-c", "/opt/apache/ranger-admin/ranger-admin-start.sh"]
