# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project
ARG REGISTRY=registry-1.docker.io
ARG REPO=odpi
FROM $REGISTRY/$REPO/apache-atlas:build AS build
FROM openjdk:8-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "apache-atlas"
LABEL org.label-schema.description = "Apache Atlas image to support ODPi Egeria demonstrations."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/apache-atlas"
LABEL org.label-schema.docker.cmd = "docker run -d -p 21000:21000 odpi/apache-atlas"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"

ENV JAVA_TOOL_OPTIONS="-Xmx1024m" \
    HBASE_CONF_DIR="/opt/apache/atlas/hbase/conf" \
    ATLAS_OPTS="-Dkafka.advertised.hostname=localhost"

RUN apk --no-cache add python bash shadow && \
    apk --no-cache update && \
    apk --no-cache upgrade && \
    groupadd -r atlas -g 21000 && \
    useradd --no-log-init -r -g atlas -u 21000 -d /opt/apache/atlas atlas

COPY --from=build --chown=atlas:atlas /opt/atlas-bin/ /opt/apache/atlas/

# Must use numeric userid here to meet k8s security checks
USER 21000:21000

WORKDIR /opt/apache/atlas
RUN sed -i "s|^atlas.graph.storage.lock.wait-time=10000|atlas.graph.storage.lock.wait-time=200|g" conf/atlas-application.properties && \
    echo "atlas.notification.relationships.enabled=true" >> conf/atlas-application.properties && \
    echo "atlas.kafka.listeners=PLAINTEXT://:9027" >> conf/atlas-application.properties && \
    echo "atlas.kafka.advertised.listeners=PLAINTEXT://\${sys:kafka.advertised.hostname}:9027" >> conf/atlas-application.properties

EXPOSE 9026 9027 21000
ENTRYPOINT ["/bin/bash", "-c", "/opt/apache/atlas/bin/atlas_start.py; tail -fF /opt/apache/atlas/logs/atlas*.out"]
