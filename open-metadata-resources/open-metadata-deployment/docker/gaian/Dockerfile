# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM openjdk:8-jre-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "gaian-egeriavdc"
LABEL org.label-schema.description = "Image containing Gaian (https://github.com/gaiandb/gaiandb) for use in ODPi Egeria Virtual Data Connector demonstration."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/gaian"
LABEL org.label-schema.docker.cmd = "docker run -d -p 6414:6414 odpi/gaian-egeriavdc"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"

# Working directory
WORKDIR /root/gaian
RUN wget https://github.com/gaiandb/gaiandb/blob/master/build/GAIANDB_V2.1.8_20160523.zip?raw=true -O GAIANDB.zip
RUN unzip GAIANDB.zip
RUN apk --no-cache add bash && apk --no-cache update && apk --no-cache upgrade

EXPOSE 6414

CMD ["/bin/bash", "-c", "/root/gaian/launchGaianServer.sh"]
