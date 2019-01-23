#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

# Script to configure Egeria - see below for variables
# used. Primarily intended as part of automated kubernetes/docker setup,
# but can also be used interactively if required.

printf "\nConfiguring Egeria for VDC\n\n" 

fail=0

if [ "null" = "$EGERIA_ENDPOINT" ] || [ -z $EGERIA_ENDPOINT ]
then
	echo "EGERIA_ENDPOINT must be supplied. This is URL of the Egeria server that will be configured. An example is 'http://egeria.acme.org:9000'"
	fail=1
fi

if [ "null" = "$KAFKA_ENDPOINT" ] || [ -z $KAFKA_ENDPOINT ]
then
	echo "KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$EGERIA_USER" ] || [ -z $EGERIA_USER ]
then
	echo "EGERIA_USER must be supplied. This is user name for the egeria api. An example is 'admin'"
	fail=1
fi

if [ "null" = "$EGERIA_SERVER" ] || [ -z $EGERIA_SERVER ]
then
	echo "EGERIA_SERVER must be supplied. This is the server name for the egeria api. An example is 'myserver'"
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


echo "Egeria server URL : ${EGERIA_ENDPOINT}"
echo "Kafka broker      : ${KAFKA_ENDPOINT}"
echo "Egeria user name  : ${EGERIA_USER}"
echo "Egeria server name: ${EGERIA_SERVER}"
echo "Egeria Cohort     : ${EGERIA_COHORT}"

echo "Checking EGERIA is up"

loop=100
retrytimeout=10
delay=30

# - In a standard environment, return code 1 indicates service is running. However in a k8s environment the 
# service appears to respond even before the backend is up. Thus a better test is to force an error.
# So we will do a post, expecting a 401 back when things are ok (full authentication is required)
while [ $loop -gt 0 ]
do
    http --check-status --ignore-stdin --timeout=${retrytimeout} HEAD ${EGERIA_ENDPOINT} &> /dev/null
    rc=$?
    if [ $rc -eq 4 ]
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
	echo "Egeria server was unavailable. Abandoning configuration. Check endpoint URL"
	exit 1
fi


# Issue requests against Egeria
http --verbose --ignore-stdin \
	--check-status \
	--auth admin:admin \
	POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/event-bus   \
	producer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' \
        consumer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' 
rc=$?
if [ $rc -ne 0 ]
then
	printf "\nEgeria setup Failed configuring event bus\n" 
	exit 1
fi


# Enable Access Services

http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
        POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/access-services?serviceMode=ENABLED
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nEgeria setup Failed enabling access services\n"
        exit 1
fi

# Server root configuration
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
        POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/server-url-root?url=${EGERIA_ENDPOINT}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nEgeria setup Failed configuring root\n"
        exit 1
fi

# Server in-memory repo
http --verbose --ignore-stdin \
        --check-status \
        --auth admin:admin \
        POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/local-repository/mode/in-memory-repository
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nEgeria setup Failed setting in-memory repo\n"
        exit 1
fi

# Cohort configuration
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
        POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/cohorts/${EGERIA_COHORT}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nEgeria setup Failed configuring cohort\n"
        exit 1
fi

# Activation - note 5 minute timeout
echo "Configuration complete -- now activating (may take a few minutes)"
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
	--timeout 900 \
        POST ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/instance
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nEgeria setup Failed starting instance\n"
        exit 1
fi
printf "\nEgeria setup completed\n"

echo "WARNING - Now going into sleep loop - to maintain health successful pod status"
while [ true ]
do
	sleep 100000000
done
exit 0
