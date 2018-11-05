#
# ---
# Docker configuration file for Ranger
#
# Nigel Jones
# may 2017
#
# Caveats:
#   * Lots of hardcoded paths
#   * No containers in use yet so data will be lost each time
# ---

# Let's use UBUNTU
FROM            ubuntu:18.04

# any problems let me know
MAINTAINER      Nigel Jones <nigel.l.jones+gh@gmail.com>

# Will use these more in future

ENV		 rootpw			 admin99
ENV		 build 			 ranger-1.2.0
ENV		 dadir			 /opt/ranger-1.2.0-admin

# Let's start with some core packages we need
RUN apt-get update && apt-get install -y wget libmysql-java python openjdk-8-jdk maven git iputils-ping net-tools vim lsof apt-utils build-essential lsb-core lsb-release

# It turns out mvn 3.5.3 (current on ubuntu distro) nor 3.5.2 work with ranger. 3.5.0 or 3.5.4 only
WORKDIR /root
RUN wget http://mirror.vorboss.net/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
RUN gunzip -c apache-maven-3.5.4-bin.tar.gz | tar -xvf -


# Setup some environment variables for later
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Add Java 8 and Maven to the path.
ENV PATH /root/apache-maven-3.5.4/bin:/usr/lib/jvm/java-8-openjdk-amd64/bin:/usr/local/apache-maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

 # Working directory - root home
WORKDIR /root

 # Pull down Ranger and build it
RUN git clone http://github.com/apache/ranger.git -b ranger-1.2

 # Remove -DskipTests if unit tests are to be included - build the code...
WORKDIR /root/ranger

# -Pall is normally used on non-linux platforms as it skips the native security plugins. However the -Plinux profile
# is currently pulling in a later maven assembly plugin as of 2018.11.05 which is breaking the build/packaging for
# the 1.2 branch....
RUN mvn -Pall -DskipTests=true clean compile package install assembly:assembly

# We have now built the latest ranger from source - let's unpack and setup the admin server & UI

# Add dockerize wait tool - this allows VMs to wait for network ports to be available.
# Only needed if running in a docker-compose type environment, not for single image
# this is recommended on docker.com
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.6.1.tar.gz



# After a build, the ranger directory looks like this - we need to chose the zips we're interested in deploying
# in this docker image
#
# antrun                                      ranger-1.2.0-migration-util.tar.gz
# archive-tmp                                 ranger-1.2.0-migration-util.zip
# maven-shared-archive-resources              ranger-1.2.0-ranger-tools.tar.gz
# ranger-1.2.0-admin.tar.gz          ranger-1.2.0-ranger-tools.zip
# ranger-1.2.0-admin.zip             ranger-1.2.0-solr-plugin.tar.gz
# ranger-1.2.0-atlas-plugin.tar.gz   ranger-1.2.0-solr-plugin.zip
# ranger-1.2.0-atlas-plugin.zip      ranger-1.2.0-sqoop-plugin.tar.gz
# ranger-1.2.0-hbase-plugin.tar.gz   ranger-1.2.0-sqoop-plugin.zip
# ranger-1.2.0-hbase-plugin.zip      ranger-1.2.0-src.tar.gz
# ranger-1.2.0-hdfs-plugin.tar.gz    ranger-1.2.0-src.zip
# ranger-1.2.0-hdfs-plugin.zip       ranger-1.2.0-storm-plugin.tar.gz
# ranger-1.2.0-hive-plugin.tar.gz    ranger-1.2.0-storm-plugin.zip
# ranger-1.2.0-hive-plugin.zip       ranger-1.2.0-tagsync.tar.gz
# ranger-1.2.0-kafka-plugin.tar.gz   ranger-1.2.0-tagsync.zip
# ranger-1.2.0-kafka-plugin.zip      ranger-1.2.0-usersync.tar.gz
# ranger-1.2.0-kms.tar.gz            ranger-1.2.0-usersync.zip
# ranger-1.2.0-kms.zip               ranger-1.2.0-yarn-plugin.tar.gz
# ranger-1.2.0-knox-plugin.tar.gz    ranger-1.2.0-yarn-plugin.zip
# ranger-1.2.0-knox-plugin.zip       rat.txt
# ranger-1.2.0-kylin-plugin.tar.gz   version
# ranger-1.2.0-kylin-plugin.zip

# Just going with the admin server here - v1 of the docker image used to copy in, so here we will just link

WORKDIR /opt
RUN gunzip -c /root/ranger/target/ranger-1.2.0-admin.tar.gz | tar -xvf -

# Get the mysql connector ready

# Setup solr - needs java, contributed script for local solr install
#
# Script#s default port is 6083. 
WORKDIR /opt/ranger-1.2.0-admin/contrib/solr_for_audit_setup
RUN sed -i 's|SOLR_INSTALL=false|SOLR_INSTALL=true|' install.properties
RUN sed -i 's|SOLR_DOWNLOAD_URL=|SOLR_DOWNLOAD_URL=http://www.mirrorservice.org/sites/ftp.apache.org/lucene/solr/7.5.0/solr-7.5.0.tgz|' install.properties

#Update path to include java, so that when we run the container it is available
#default shell isn't bash :-(

#RUN echo "export JAVA_HOME=/usr/lib/jvm/default-java" >> /root/.bashrc
RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" >> /root/.profile
RUN echo "export PATH=${JAVA_HOME}/bin:$PATH" >> /root/.profile
RUN chmod +x /root/.profile


# This should work according to docs, but see RANGER-1565 -
#
# --RUN sed -i 's|#JAVA_HOME=|JAVA_HOME=/usr/lib/jvm/default-java|' install.properties
# --RUN ./setup.sh
#
# So this is the workaround
RUN JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 ./setup.sh

# setup the ranger server
WORKDIR /opt/ranger-1.2.0-admin
RUN sed -i 's|^db_root_password=|db_root_password=admin99|g' "${dadir}/install.properties" 
RUN sed -i 's|^db_password=|db_password=admin|g' "${dadir}/install.properties" 
RUN sed -i 's|^db_host=localhost|db_host=localhost:3306|g' "${dadir}/install.properties" 
RUN sed -i 's|^audit_solr_urls=|audit_solr_urls=http://localhost:6083/solr/ranger_audits|g' install.properties
RUN sed -i 's|^rangerAdmin_password=|rangerAdmin_password=admin999|g' "${dadir}/install.properties"
RUN sed -i 's|^rangerTagsync_password=|rangerTagsync_password=admin999|g' "${dadir}/install.properties"
RUN sed -i 's|^rangerUsersync_password=|rangerUsersync_password=admin999|g' "${dadir}/install.properties"
RUN sed -i 's|^keyadmin_password=|keyadmin_password=admin999|g' "${dadir}/install.properties"

# Expose web ports
EXPOSE          6080 6182 6083 6183

# Build launch script which can be started from docker-compose
# TODO - this could be moved to a dist. script

RUN echo "#/bin/sh" > /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64"  >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "export PATH=${JAVA_HOME}:$PATH"  >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh

RUN echo "/usr/local/bin/dockerize -wait tcp:/localhost:3306 -timeout 120s" >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "/opt/ranger-1.2.0-admin/setup.sh" >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh

# Now let's start solr 
RUN echo "/opt/solr/ranger_audit_server/scripts/start_solr.sh &" >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh

RUN echo "/usr/bin/ranger-admin start" >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN chmod +x /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "while [ true ]" >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "do"  >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "  sleep 10000"  >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh
RUN echo "done"  >> /opt/ranger-1.2.0-admin/ranger-docker-start.sh

# Hopefully running that script will finish the configuration (which needs mysql)
ENTRYPOINT ["/bin/sh","-c","/opt/ranger-1.2.0-admin/ranger-docker-start.sh"]
