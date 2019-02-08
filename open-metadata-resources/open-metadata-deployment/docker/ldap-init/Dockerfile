# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.04

LABEL org.label-schema.name = "LDAPInitEgeriaVDC"
LABEL org.label-schema.description="Initialization for ldap"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/ldap-init"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git git-extras netcat dnsutils ldapscripts

# Stup the cohort (cannot set empty string)
ENV LDAP_ENDPOINT null
ENV LDAP_USER null
ENV LDAP_PASS null

WORKDIR /root
COPY dist/configure-ldap.sh /root

ARG              branch
ENV              branch                  ${branch:-master}

ARG              pr

RUN git clone http://github.com/odpi/egeria.git -b ${branch}
WORKDIR /root/egeria
# Add Pull request if specified (requires git-extras package)
RUN if [ ! -z "$pr" ] ; then git pr $pr ; fi
WORKDIR /root

ENTRYPOINT ./configure-ldap.sh 



