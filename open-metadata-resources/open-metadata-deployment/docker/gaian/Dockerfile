# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM ubuntu:16.04

MAINTAINER      Nigel Jones <nigel.l.jones+gh@gmail.com>

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
#CMD ["/bin/bash", "-c", "sleep 10000000"]
