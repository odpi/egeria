# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM centos:7

LABEL org.label-schema.name = "PostgresqlInitEgeriaVDC"
LABEL org.label-schema.description="Initialization for postgresql"
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/postgresql-init"
LABEL org.label-schema.version="0.1.0"
LABEL org.label-schema.schema-version = "1.0"

# Install Git, which is missing from the Ubuntu base images.
RUN yum -y install epel-release && yum install -y git ansible  python-lxml python-pip git-extras nc httpie bind-utils
RUN yum -y update
RUN pip install --upgrade pip
RUN pip install --upgrade lxml
RUN pip install jmespath

# Add ansible roles needed for subsequent samples
RUN ansible-galaxy install IBM.infosvr && ansible-galaxy install IBM.infosvr-metadata-asset-manager && ansible-galaxy install IBM.infosvr-import-export

# TODO 
ENV POSTGRESQL_SERVER null
ENV POSTGRESQL_SERVICE_PORT null
ENV POSTGRESQL_OS_USER null
ENV POSTGRESQL_OS_PASS null
ENV IGC_SERVER null
ENV IGC_SERVICE_PORT null
ENV IGC_OS_USER null
ENV IGC_OS_PASS null
ENV KAFKA_ENDPOINT null

WORKDIR /root
COPY dist/configure-postgresql.sh /root

ARG              branch
ENV              branch                  ${branch:-master}

ARG              pr

RUN git clone http://github.com/odpi/egeria.git -b ${branch}
WORKDIR /root/egeria
# Add Pull request if specified (requires git-extras package)
RUN if [ ! -z "$pr" ] ; then git pr $pr ; fi
WORKDIR /root

ENTRYPOINT ./configure-postgresql.sh 



