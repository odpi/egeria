# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM node:10-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "omrsmonitor-egeriavdc"
LABEL org.label-schema.description = "Image containing a simple visualisation of the Open Metadata Repository Services (OMRS) cohort, for ODPi Egeria demonstration purposes."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/omrs-monitor"
LABEL org.label-schema.docker.cmd = "docker run -d -p 58080:58080 -e KAFKA_ENDPOINT=host:9092 odpi/omrsmonitor-egeriavdc"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "KAFKA_ENDPOINT=the hostname and port of a Kafka broker in the OMRS cohort"

RUN mkdir -p /home/node/monitor/node_modules && chown -R node:node /home/node/monitor

WORKDIR /home/node/monitor

COPY dist/package*.json ./

RUN npm install

COPY dist/* ./

COPY --chown=node:node . .

USER node

EXPOSE 58080

ENV KAFKA_ENDPOINT $KAFKA_ENDPOINT

CMD [ "node", "monitor.js" ]



