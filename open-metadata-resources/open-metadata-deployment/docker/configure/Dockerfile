# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM alpine:3.11.2

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "egeria-configure"
LABEL org.label-schema.description = "Common image used to configure various pieces of ODPi Egeria demonstrations. By default, checks that a Kubernetes service provided by the SERVICE environment variable is available (reliant on Kubernetes)."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/configure"
LABEL org.label-schema.docker.cmd = "docker run -d odpi/egeria-configure"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "SERVICE=the name of the k8s service to check is available"

# Install utilities we will use to configure containers
RUN apk --no-cache add bash shadow curl git jq openldap-clients postgresql-client mariadb-client && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r config -g 1000 && useradd --no-log-init -r -g config -u 1000 -d /opt/config config

COPY --chown=config:config dist/check-availability.sh /check-availability.sh

WORKDIR /opt/config
USER config:config

ENTRYPOINT ["/check-availability.sh"]
