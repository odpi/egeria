#!/bin/sh

sed -i "s|^db_root_user=root|db_root_user=${PGUSER}|g" "install.properties"
sed -i "s|^db_root_password=$|db_root_password=${PGPASSWORD}|g" "install.properties"
sed -i "s|^db_password=$|db_password=${PGPASSWORD}|g" "install.properties"
sed -i "s|^db_host=localhost|db_host=${PGHOST}:5432|g" "install.properties"
sed -i "s|^rangerAdmin_password=$|rangerAdmin_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^rangerTagsync_password=$|rangerTagsync_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^rangerUsersync_password=$|rangerUsersync_password=${RANGER_PASSWORD}|g" "install.properties"
sed -i "s|^keyadmin_password=$|keyadmin_password=${RANGER_PASSWORD}|g" "install.properties"

su -c "./setup.sh" ranger

# Now let's start solr
/opt/solr/ranger_audit_server/scripts/start_solr.sh

su -c "./ews/ranger-admin-services.sh start" ranger

sleep 5

su -c "tail -F ews/logs/ranger-admin*.log" ranger
