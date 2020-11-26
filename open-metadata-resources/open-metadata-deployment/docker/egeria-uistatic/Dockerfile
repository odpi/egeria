# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM node:14-alpine as build

ARG version=2.6-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor = "ODPi" \
      org.opencontainers.image.title = "Egeria-uistatic" \
      org.opencontainers.image.description = "Static Web Content to support the ui server chassis" \
      org.opencontainers.image.url = "https://egeria.odpi.org/" \
      org.opencontainers.image.source = "$VCS_ORIGIN" \
      org.opencontainers.image.authors = "ODPi Egeria" \
      org.opencontainers.image.revision = "$VCS_REF" \
      org.opencontainers.image.licenses = "Apache-2.0" \
      org.opencontainers.image.created = "$BUILD_TIME" \
      org.opencontainers.image.version = "$version" \
      org.opencontainers.image.documentation = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria-uistatic/" \
      org.opencontainers.image.ext.vcs-date = "$VCS_DATE" \
      org.opencontainers.image.ext.docker.cmd = "docker run -p 8080:8080 -d odpi/egeria-uistatic" \
      org.opencontainers.image.ext.docker.debug = "" \
      org.opencontainers.image.ext.docker.params = ""

# Initial version -- build from git
RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh

WORKDIR /app
RUN git clone https://github.com/odpi/egeria-ui
WORKDIR egeria-ui
RUN npm install
RUN npm run build


FROM nginx:latest

COPY dist/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/egeria-ui/build/prod/ /var/www
CMD ["nginx", "-g", "daemon off;"]

