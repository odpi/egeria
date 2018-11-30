# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM ubuntu:18.04

# Image metadata following http://label-schema.org/rc1/
LABEL org.label-schema.name = "GaianForEgeriaVDC"
LABEL org.label-schema.description="Gaian service to support Egeria VDC demos"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/gaian"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"


RUN apt-get update && apt-get -y upgrade && apt-get install -y openjdk-8-jdk git python unzip wget
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

ENV PATH /usr/lib/jvm/java-8-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root/gaian
RUN wget https://github.com/gaiandb/gaiandb/blob/master/build/GAIANDB_V2.1.8_20160523.zip?raw=true
RUN unzip GAIANDB_V2.1.8_20160523.zip?raw=true

# Working directory
WORKDIR /root/gaian

ENV PATH /root/atlas-bin/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

EXPOSE 6414

CMD ["/bin/bash", "-c", "/root/gaian/launchGaianServer.sh"]
