# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM jupyter/base-notebook:latest
ARG version=1.5-SNAPSHOT
ARG VCS_REF=unknown
ARG VCS_ORIGIN=unknown
ARG BUILD_TIME=unknown
ARG VCS_DATE=unknown

# Standard in Jupyter stacks
#ENV NB_USER jovyan
#ENV HOME /home/$NB_USER

ENV version ${version}

# Labels from https://github.com/opencontainers/image-spec/blob/master/annotations.md#pre-defined-annotation-keys (with additions prefixed ext)
LABEL org.opencontainers.image.vendor = "ODPi" \
      org.opencontainers.image.title = "Jupyter" \
      org.opencontainers.image.description = "Customized Jupyter notebook environment for ODPi Egeria" \
      org.opencontainers.image.url = "https://egeria.odpi.org/" \
      org.opencontainers.image.source = "$VCS_ORIGIN" \
      org.opencontainers.image.authors = "ODPi Egeria" \
      org.opencontainers.image.revision = "$VCS_REF" \
      org.opencontainers.image.licenses = "Apache-2.0" \
      org.opencontainers.image.created = "$BUILD_TIME" \
      org.opencontainers.image.version = "$version" \
      org.opencontainers.image.documentation = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/jupyter/" \
      org.opencontainers.image.ext.vcs-date = "$VCS_DATE"

USER root
RUN apt-get update && apt-get -y install git --no-install-recommends && rm -rf /var/lib/apt/lists/*

USER $NB_USER
# Leave EXPOSE, ENTRYPOINT and CMD as per official images
