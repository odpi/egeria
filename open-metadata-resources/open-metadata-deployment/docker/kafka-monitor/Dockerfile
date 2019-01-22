# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.04

LABEL org.label-schema.name = "KafkaMonitorEgeriaVDC"
LABEL org.label-schema.description="Monitor for Kafka in Egeria VDC"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/kafka-monitor"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

RUN apt-get update && apt-get install -y openjdk-8-jdk wget inetutils-ping dnsutils gzip tar

# Install Java.
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Add Java and Maven to the path.
ENV PATH /usr/java/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root

# Download kafka for debugging manually (via kubectl exec)
RUN wget http://mirror.ox.ac.uk/sites/rsync.apache.org/kafka/2.1.0/kafka_2.12-2.1.0.tgz
RUN gunzip -c kafka_2.12-2.1.0.tgz | tar -xvf -


RUN apt-get update && apt-get install -y kafkacat

COPY dist/kafka-tail.sh .

# Contains a list of topics to monitor
ENV KAFKA_OMRS_TOPIC null
ENV KAFKA_ENDPOINT  null

ENTRYPOINT [ "/bin/sh","-c",""dist/entrypoint.sh"



