# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.04

LABEL org.label-schema.name = "CohortInitEgeriaVDC"
LABEL org.label-schema.description="Initialization for Egeria cohort"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/atlas-init"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget httpie

# Stup the cohort (cannot set empty string)
ENV ATLAS_ENDPOINT null
ENV KAFKA_ENDPOINT null
ENV EGERIA_USER null
ENV EGERIA_SERVER null
ENV EGERIA_COHORT null

WORKDIR /root
COPY dist/configure-atlas.sh /root


ENTRYPOINT ./configure-atlas.sh 



