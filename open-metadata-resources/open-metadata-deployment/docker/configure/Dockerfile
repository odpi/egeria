# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM docker.io/library/alpine:3.18.3
ARG version=4.4-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor = "ODPi" \
      org.opencontainers.image.title = "Egeria-configure" \
      org.opencontainers.image.description = "Common image used to configure various pieces of ODPi Egeria demonstrations. By default, checks that a Kubernetes service provided by the SERVICE environment variable is available (reliant on Kubernetes)." \
      org.opencontainers.image.url = "https://egeria.odpi.org/" \
      org.opencontainers.image.source = "$VCS_ORIGIN" \
      org.opencontainers.image.authors = "ODPi Egeria" \
      org.opencontainers.image.revision = "$VCS_REF" \
      org.opencontainers.image.licenses = "Apache-2.0" \
      org.opencontainers.image.created = "$BUILD_TIME" \
      org.opencontainers.image.version = "$version" \
      org.opencontainers.image.documentation = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria/" \
      org.opencontainers.image.ext.vcs-date = "$VCS_DATE" \
      org.opencontainers.image.ext.docker.cmd = "docker run -d odpi/egeria-configure" \
      org.opencontainers.image.ext.docker.debug = "docker exec -it $CONTAINER /bin/sh" \
      org.opencontainers.image.ext.docker.params = "SERVICE=the name of the k8s service to check is available"

# Install utilities we will use to configure containers
RUN apk --no-cache add bash shadow curl git jq openldap-clients postgresql-client mariadb-client && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r config -g 1000 && useradd --no-log-init -r -g config -u 1000 -d /opt/config config

COPY --chown=config:config dist/check-availability.sh /check-availability.sh
COPY --chown=config:config dist/.curlrc /opt/config/.curlrc
WORKDIR /opt/config
# Must use numeric userid here to meet k8s security checks
USER 1000:1000



ENTRYPOINT ["/check-availability.sh"]
