#!/bin/sh

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

# Script to configure Atlas for use with egeria - see below for variables
# used. Primarily intended as part of automated kubernetes/docker setup,
# but can also be used interactively if required.

printf "\nConfiguring Atlas for Egeria VDC\n\n" 

fail=0

if [ "null" = "$ATLAS_ENDPOINT" ] || [ -z $ATLAS_ENDPOINT ]
then
	echo "ATLAS_ENDPOINT must be supplied. This is URL of the Atlas server that will be configured. An example is 'http://atlas.acme.org:9000'"
	fail=1
fi

if [ "null" = "$KAFKA_ENDPOINT" ] || [ -z $KAFKA_ENDPOINT ]
then
	echo "KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$EGERIA_USER" ] || [ -z $EGERIA_USER ]
then
	echo "EGERIA_USER must be supplied. This is user name for the egeria api in Atlas. An example is 'admin'"
	fail=1
fi

if [ "null" = "$EGERIA_SERVER" ] || [ -z $EGERIA_SERVER ]
then
	echo "EGERIA_SERVER must be supplied. This is the server name for the egeria api in Atlas. An example is 'myserver'"
	fail=1
fi

if [ "null" = "$EGERIA_COHORT" ] || [ -z $EGERIA_COHORT ]
then
	echo "EGERIA_COHORT must be supplied. This is the name of the cohort. An example is 'coco'"
	fail=1
fi

if [ $fail -eq 1 ]
then
	exit 1
fi

# TODO: we should have a valid endpoint now. Check a basic 'ping'

echo "Atlas server URL  : ${ATLAS_ENDPOINT}"
echo "Kafka broker      : ${KAFKA_ENDPOINT}"
echo "Egeria user name  : ${EGERIA_USER}"
echo "Egeria server name: ${EGERIA_SERVER}"
echo "Egeria Cohort     : ${EGERIA_COHORT}"

# Isue requests against atlas
http --verbose --ignore-stdin \
	--auth admin:admin \
	POST ${ATLAS_ENDPOINT}/egeria/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/event-bus   \
	producer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' \
        consumer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' 
rc=$?
if [ $rc -ne 0 ]
then
	printf "\nAtlas setup Failed configuring event bus\n" 
	exit 1
fi


# Repository type configuration

http --verbose --ignore-stdin \
        --auth admin:admin \
        POST ${ATLAS_ENDPOINT}/egeria/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/atlas-repository   
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nAtlas setup Failed configuring repository type\n"
        exit 1
fi

# Server root configuration
http --verbose --ignore-stdin \
        --auth admin:admin \
        POST ${ATLAS_ENDPOINT}/egeria/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/server-url-root?url=${ATLAS_ENDPOINT}/egeria
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nAtlas setup Failed configuring root\n"
        exit 1
fi

# Cohort configuration
http --verbose --ignore-stdin \
        --auth admin:admin \
        POST ${ATLAS_ENDPOINT}/egeria/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/cohorts/${EGERIA_COHORT}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nAtlas setup Failed configuring cohort\n"
        exit 1
fi

# Activation - note 5 minute timeout
echo "Configuration complete -- now activating (may take a few minutes)"
http --verbose --ignore-stdin \
        --auth admin:admin \
	--timeout 900 \
        POST ${ATLAS_ENDPOINT}/egeria/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/instance
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nAtlas setup Failed starting instance\n"
        exit 1
fi
printf "\nAtlas setup completed\n"
exit 0
