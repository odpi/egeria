# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.04

LABEL org.label-schema.name = "PostgreSQLForEgeriaVDC"
LABEL org.label-schema.description="PostgreSQL service to support Egeria VDC demos" 
LABEL org.label-schema.url="https://github.com/odpi/egeria/"
LABEL org.label-schema.vcs="https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/postgresql"
LABEL org.label-schema.version="0.1.4"
LABEL org.label-schema.schema-version = "1.0"

# Add SSH server - needed for remote configuration by ansible

#
# example Dockerfile for https://docs.docker.com/engine/examples/postgresql_service/
#
RUN apt-get update && apt-get install -y postgresql openssh-server sed python python3 curl rsyslog sudo

RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN mkdir /.ssh;chown -R root:root /.ssh;chmod -R 700 /.ssh
RUN rm -f /etc/ssh/ssh_host_*
RUN mkdir -p /var/run/sshd

# Add extra python tools needed for ansible
RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py; chmod +x ./get-pip.py; ./get-pip.py
RUN pip install lxml jmespath 

# Set password for ssh
RUN echo 'root:passw0rd' | chpasswd
EXPOSE 22

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible from any IP
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/10/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/10/main/postgresql.conf

# Expose the PostgreSQL port & ssh§
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
# TODO - non persistant for now
#VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

ADD dist/startpg.sh /root

RUN /usr/bin/ssh-keygen -A
# Launch postgres
CMD ["/root/startpg.sh"]
