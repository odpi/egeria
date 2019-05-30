# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM ubuntu:18.10 AS build

# Install Git, which is missing from the Ubuntu base images.
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget inetutils-ping dnsutils gzip tar
RUN apt-get update && apt-get install -y git python openjdk-8-jdk maven wget libpostgresql-jdbc-java iputils-ping net-tools vim lsof apt-utils build-essential lsb-core lsb-release

ARG branch
ENV branch ${branch:-master}

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV MAVEN_HOME /usr/share/maven
ENV PATH /usr/java/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# Working directory
WORKDIR /root

# Pull down Ranger and build it
RUN git clone http://github.com/apache/ranger.git -b ${branch}

# Remove -DskipTests if unit tests are to be included - build the code...
WORKDIR /root/ranger

# -Pall is normally used on non-linux platforms as it skips the native security plugins. However the -Plinux profile
# is currently pulling in a later maven assembly plugin as of 2018.11.05 which is breaking the build/packaging for
# the 1.2 branch....
RUN mvn -Pall -DskipTests=true clean compile package install assembly:assembly
RUN git log > /git.log && echo ${branch} > /git.branch && git remote -v > /git.remote

# We have now built the latest ranger from source - let's unpack and setup the admin server & UI
# After a build, the target directory looks like this - we need to chose the zips we're interested in deploying
# in this docker image
#
# antrun                                      ranger-2.2.0-migration-util.tar.gz
# archive-tmp                                 ranger-2.2.0-migration-util.zip
# maven-shared-archive-resources              ranger-2.2.0-ranger-tools.tar.gz
# ranger-2.2.0-admin.tar.gz          ranger-2.2.0-ranger-tools.zip
# ranger-2.2.0-admin.zip             ranger-2.2.0-solr-plugin.tar.gz
# ranger-2.2.0-atlas-plugin.tar.gz   ranger-2.2.0-solr-plugin.zip
# ranger-2.2.0-atlas-plugin.zip      ranger-2.2.0-sqoop-plugin.tar.gz
# ranger-2.2.0-hbase-plugin.tar.gz   ranger-2.2.0-sqoop-plugin.zip
# ranger-2.2.0-hbase-plugin.zip      ranger-2.2.0-src.tar.gz
# ranger-2.2.0-hdfs-plugin.tar.gz    ranger-2.2.0-src.zip
# ranger-2.2.0-hdfs-plugin.zip       ranger-2.2.0-storm-plugin.tar.gz
# ranger-2.2.0-hive-plugin.tar.gz    ranger-2.2.0-storm-plugin.zip
# ranger-2.2.0-hive-plugin.zip       ranger-2.2.0-tagsync.tar.gz
# ranger-2.2.0-kafka-plugin.tar.gz   ranger-2.2.0-tagsync.zip
# ranger-2.2.0-kafka-plugin.zip      ranger-2.2.0-usersync.tar.gz
# ranger-2.2.0-kms.tar.gz            ranger-2.2.0-usersync.zip
# ranger-2.2.0-kms.zip               ranger-2.2.0-yarn-plugin.tar.gz
# ranger-2.2.0-knox-plugin.tar.gz    ranger-2.2.0-yarn-plugin.zip
# ranger-2.2.0-knox-plugin.zip       rat.txt
# ranger-2.2.0-kylin-plugin.tar.gz   version
# ranger-2.2.0-kylin-plugin.zip

# Just going with the admin server here -- to include others, extract to a unique location and then copy path across
# to runtime image below
RUN mkdir -p /opt/ranger-admin
RUN tar xzf /root/ranger/target/ranger-*-admin.tar.gz --strip-components 1 -C /opt/ranger-admin

COPY dist/ranger-docker-start.sh /opt/ranger-admin/ranger-docker-start.sh
RUN chmod 755 /opt/ranger-admin/ranger-docker-start.sh

# Configure and install Solr locally
WORKDIR /opt/ranger-admin/contrib/solr_for_audit_setup
RUN sed -i 's|SOLR_INSTALL=false|SOLR_INSTALL=true|' install.properties
RUN sed -i "s|SOLR_DOWNLOAD_URL=|SOLR_DOWNLOAD_URL=http://www.mirrorservice.org/sites/ftp.apache.org/lucene/solr/7.7.1/solr-7.7.1.tgz|" install.properties
RUN ./setup.sh
RUN sed -i 's|^SOLR_JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"$|SOLR_JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"|g' /opt/solr/ranger_audit_server/scripts/solr.in.sh

WORKDIR /opt/ranger-admin
RUN sed -i "s|^DB_FLAVOR=MYSQL|DB_FLAVOR=POSTGRES|g" install.properties
RUN sed -i "s|^SQL_CONNECTOR_JAR=/usr/share/java/mysql-connector-java.jar|SQL_CONNECTOR_JAR=/usr/share/java/postgresql-jdbc.jar|g" install.properties
RUN sed -i "s|^audit_solr_urls=|audit_solr_urls=http://localhost:6083/solr/ranger_audits|g" install.properties

# Create minimal runtime image
FROM openjdk:8-jdk-alpine

LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "ODPi"
LABEL org.label-schema.name = "ranger-admin-egeriavdc"
LABEL org.label-schema.description = "Apache Ranger administration server, for use as part of ODPi Egeria Virtual Data Connector demonstrations."
LABEL org.label-schema.url = "https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/"
LABEL org.label-schema.vcs-url = "https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/docker/ranger"
LABEL org.label-schema.docker.cmd = "docker run -d -p 6080:6080 -p 6182:6182 -p 6083:6083 -p 6183:6183 -e PGUSER=postgres -e PGPASSWORD=passw0rd -e PGHOST=host -e RANGER_PASSWORD=admin999 odpi/ranger-admin-egeriavdc"
LABEL org.label-schema.docker.debug = "docker exec -it $CONTAINER /bin/sh"
LABEL org.label-schema.docker.params = "PGUSER=postgres user,PGPASSWORD=postgres password,PGHOST=postgres host,RANGER_PASSWORD=password to init for ranger admin user"

RUN apk --no-cache add python bash java-postgresql-jdbc bc shadow procps && apk --no-cache update && apk --no-cache upgrade

RUN groupadd -r ranger -g 1000 && useradd --no-log-init -r -g ranger -u 1000 -d /opt/ranger-admin ranger
RUN groupadd -r solr -g 1001 && useradd --no-log-init -r -g solr -u 1001 -d /opt/solr solr && mkdir -p /var/log/solr/ranger_audits && chown solr:solr /var/log/solr/ranger_audits

COPY --from=build --chown=ranger:ranger /opt/ranger-admin/ /opt/ranger-admin/
COPY --from=build --chown=ranger:ranger /git.* /opt/details/
COPY --from=build --chown=solr:solr /opt/solr/ /opt/solr/

# Once Solr is separated, we whould run this as the ranger user (non-root)
#USER ranger:ranger

WORKDIR /opt/ranger-admin

EXPOSE 6080 6182 6083 6183
ENTRYPOINT ["/bin/bash", "-c", "/opt/ranger-admin/ranger-docker-start.sh"]
