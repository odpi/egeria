# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM node:10-alpine


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



