# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM node:12-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "presentation-server"
LABEL org.label-schema.description = "Egeria presentation server"
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/presentation-server"
LABEL org.label-schema.docker.cmd = "docker run -d -p 58080:58080 odpi/presentation-server"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"

RUN mkdir -p /home/node/presentation-server/node_modules && chown -R node:node /home/node/presentation-server

WORKDIR /home/node/presentation-server

COPY target/assembly/* ./

COPY --chown=node:node . .

USER node

EXPOSE 58080


CMD [ "node", "src/server/index.js" ]
