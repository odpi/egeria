#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

# Script to configure Egeria - see below for variables
# used. Primarily intended as part of automated kubernetes/docker setup,
# but can also be used interactively if required.

printf "\nConfiguring postgreSQL for VDC\n\n" 

fail=0

if [ "null" = "$IGC_SERVER" ] || [ -z $IGC_SERVER ]
then
	echo "IGC_SERVER must be supplied. This is the hostname of the IGC server that will be configured. An example is 'igc.openmetadata.ibmcloud.com"

	fail=1
fi


if [ "null" = "$IGC_OS_USER" ] || [ -z $IGC_OS_USER ]
then
	echo "IGC_OS_USER must be supplied. This is user name for the igc system. An example is 'admin'"
	fail=1
fi

if [ "null" = "$IGC_OS_PASS" ] || [ -z $IGC_OS_PASS ]
then
	echo "IGC_OS_PASS must be supplied. This is password for the igc system. An example is 'admin'"
	fail=1
fi

if [ "null" = "$POSTGRESQL_SERVER" ] || [ -z $POSTGRESQL_SERVER ]
then
	echo "POSTGRESQL_SERVER must be specified. This is the hostname of the postgresql server, for example postgres.ibmcloud.com"
	fail=1
fi

if [ "null" = "$POSTGRESQL_SERVICE_PORT" ] || [ -z $POSTGRESQL_SERVICE_PORT ]
then
	echo "POSTGRESQL_SERVICE_PORT must be specified. This is the portnumber of the postgresql server, for example 1234"
	fail=1
fi

if [ "null" = "$IGC_SERVICE_PORT" ] || [ -z $IGC_SERVICE_PORT ]
then
	echo "IGC_SERVICE_PORT must be specified. This is the portnumber of the postgresql server, for example 1234"
	fail=1
fi

if [ "null" = "$POSTGRSQL_OS_USER" ] || [ -z $POSTGRESQL_OS_USER ]
then
	echo "IGC_USER must be supplied. This is user name for the igc system. An example is 'admin'"
	fail=1
fi

if [ "null" = "$POSTGRESQL_OS_PASS" ] || [ -z $POSTGRESQL_OS_PASS ]
then
	echo "IGC_PASSWORD must be supplied. This is password for the igc system. An example is 'admin'"
	fail=1
fi

if [ $fail -eq 1 ]
then
	exit 1
fi


echo "IGC server URL    : ${IGC_SERVER}"
echo "IGC server Port   : ${IGC_SERVICE_PORT}"
echo "PostgreSQL URL    : ${POSTGRESQL_SERVER}"
echo "PostgreSQL Port   : ${POSTGRESQL_SERVICE_PORT}"
echo "IGC User          : ${IGC_OS_USER}"
echo "Postgres User     : ${POSTGRESQL_OS_USER}"


# We check the service is up, as this probably means the machine is in a good enough state to run the required
# ansible tasks, with IGC itself running. If SSH is unavailable then ansible will discover this later.
echo "Checking IGC REST is up"

loop=100
retrytimeout=10
delay=30

IGC_ENDPOINT=https://${IGC_SERVER}:${IGC_SERVICE_PORT}/ibm/iis/igc
while [ $loop -gt 0 ]
do
    http --verify=no --check-status --ignore-stdin --timeout=${retrytimeout} ${IGC_ENDPOINT} &> /dev/null
    rc=$?
    # 3 from a '302 Found' response
    if [ $rc -eq 3 ]
    then
        echo 'OK!'
	break
    else
        # timeout or otherwise not ready - let's keep trying
        let loop=$loop-1
        echo ".. not yet up. waiting ${delay}s. ${loop} attempts remaining"
        sleep ${delay}
    fi
done


if [ $loop -le 0 ]
then
	echo "IGC server was unavailable. Abandoning configuration. Check endpoint URL"
	exit 1
fi

echo "Checking postgresql is available"

loop=100
retrytimeout=10
delay=30

echo "Checking for SSH on IGC server"
while [ $loop -gt 0 ]
do
    nc -zv ${IGC_SERVER} 22
    rc=$?
    if [ $rc -eq 0 ]
    then
        echo 'OK!'
        break
    else
        # timeout or otherwise not ready - let's keep trying
        let loop=$loop-1
        echo ".. not yet up. waiting ${delay}s. ${loop} attempts remaining"
        sleep ${delay}
    fi
done

while [ $loop -gt 0 ]
do
    nc -zv ${POSTGRESQL_SERVER} ${POSTGRESQL_SERVICE_PORT}
    rc=$?
    if [ $rc -eq 0 ]
    then
        echo 'OK!'
        break
    else
        # timeout or otherwise not ready - let's keep trying
        let loop=$loop-1
        echo ".. not yet up. waiting ${delay}s. ${loop} attempts remaining"
        sleep ${delay}
    fi
done

echo "Checking for SSH on Postgres server"
while [ $loop -gt 0 ]
do
    nc -zv ${POSTGRESQL_SERVER} 22
    rc=$?
    if [ $rc -eq 0 ]
    then
        echo 'OK!'
        break
    else
        # timeout or otherwise not ready - let's keep trying
        let loop=$loop-1
        echo ".. not yet up. waiting ${delay}s. ${loop} attempts remaining"
        sleep ${delay}
    fi
done


if [ $loop -le 0 ]
then
        echo "Postgresql server was unavailable. Abandoning configuration. Check endpoint URL"
        exit 1
fi

cd /root/egeria/open-metadata-resources/open-metadata-deployment/sample-data/coco-pharmaceuticals
if [ $? -ne 0 ]
then
	echo "Failed to locate sample databae ansible scripts to work with"
	exit 1
fi



# Now launch ansible to perform the deployment of the database -- and configure IGC
echo "[targets]" > hosts
echo "igc.openmetadata.ibmcloud.com ansible_user='${IGC_OS_USER}' ansible_password='${IGC_OS_PASS}'" >> hosts
echo "${POSTGRESQL_SERVER} ansible_user='${POSTGRESQL_OS_USER}' ansible_password='${POSTGRESQL_OS_PASS}'" >> hosts
echo "[egeria-samples-db-host]" >> hosts
echo "${POSTGRESQL_SERVER}" >> hosts
for label in egeria-samples-files-host ibm-information-server-repo ibm-information-server-domain ibm-information-server-engine
do
  echo "[${label}]" >> hosts
  echo "igc.openmetadata.ibmcloud.com" >> hosts
done

# disable host key checking for SSH
echo "host_key_checking = false" >> ansible.cfg

# create a new configuration
CONFIG=defaults/main.yml
echo "egeria_samples_cocopharma_targets:" > $CONFIG
echo "  files: /data/files/CocoPharma" >> $CONFIG
echo "  db_location: /data/db" >> $CONFIG
echo "egeria_samples_cocopharma_db_type: postgresql" >> $CONFIG
echo "egeria_samples_metadata_backend: ibm-igc" >> $CONFIG
echo "egeria_samples_db_credentials:" >> $CONFIG
echo "  owner: postgres" >> $CONFIG
echo "  group: postgres" >> $CONFIG
echo "  passwd: null" >> $CONFIG
echo "egeria_samples_metadata_credentials:" >> $CONFIG
echo "  owner: dsadm" >> $CONFIG
echo "  group: dstage" >> $CONFIG
echo "egeria_samples_db_schema: public" >> $CONFIG

ansible-playbook -i hosts deploy.yml


echo "WARNING - Now going into sleep loop - to maintain health successful pod status"
while [ true ]
do
        sleep 100000000
done
