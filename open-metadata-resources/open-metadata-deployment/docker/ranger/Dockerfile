# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

# Create minimal runtime image
FROM $REPO/ranger:build AS build
FROM openjdk:8-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "ranger-admin-egeriavdc"
LABEL org.label-schema.description = "Apache Ranger administration server, for use as part of ODPi Egeria Virtual Data Connector demonstrations."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/ranger"
LABEL org.label-schema.docker.cmd = "docker run -d -p 6080:6080 -p 6182:6182 -p 6083:6083 -p 6183:6183 -e PGUSER=postgres -e PGPASSWORD=passw0rd -e PGHOST=host -e RANGER_PASSWORD=admin999 odpi/ranger-admin-egeriavdc"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "PGUSER=postgres user,PGPASSWORD=postgres password,PGHOST=postgres host,RANGER_PASSWORD=password to init for ranger admin user"

RUN apk --no-cache add python bash java-postgresql-jdbc bc shadow procps && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r ranger -g 1000 && useradd --no-log-init -r -g ranger -u 1000 -d /opt/ranger-admin ranger
RUN groupadd -r solr -g 1001 && useradd --no-log-init -r -g solr -u 1001 -d /opt/solr solr && mkdir -p /var/log/solr/ranger_audits && chown solr:solr /var/log/solr/ranger_audits

COPY --from=build --chown=ranger:ranger /opt/ranger-admin/ /opt/ranger-admin/
COPY --from=build --chown=ranger:ranger /git.* /opt/details/
COPY --from=build --chown=solr:solr /opt/solr/ /opt/solr/

# Once Solr is separated, we whould run this as the ranger user (non-root)
#USER ranger:ranger

WORKDIR /opt/ranger-admin

EXPOSE 6080 6182 6083 6183
ENTRYPOINT ["/bin/bash", "-c", "/opt/ranger-admin/ranger-docker-start.sh"]
