#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

# Setup the configuration, as it seems only possible for Ranger to read it from 'install.properties'
sed -i "s|^db_root_user=root|db_root_user=${PGUSER}|g" "install.properties"
sed -i "s|^db_root_password=$|db_root_password=${PGPASSWORD}|g" "install.properties"
sed -i "s|^db_password=$|db_password=${PGPASSWORD}|g" "install.properties"
sed -i "s|^db_host=localhost|db_host=${PGHOST}:5432|g" "install.properties"
sed -i "s|^rangerAdmin_password=$|rangerAdmin_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^rangerTagsync_password=$|rangerTagsync_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^rangerUsersync_password=$|rangerUsersync_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^keyadmin_password=$|keyadmin_password=${RANGER_PASSWORD}|g" "install.properties"

# Run the Ranger setup (creation of database tables, directories, etc) -- needs to run as root
./setup.sh

# Now let's start Solr (will start as 'solr')
/opt/solr/ranger_audit_server/scripts/start_solr.sh

# And then Ranger itself (as 'ranger')
su -c "./ews/ranger-admin-services.sh start" ranger

sleep 10
tail -F ews/logs/*.log
