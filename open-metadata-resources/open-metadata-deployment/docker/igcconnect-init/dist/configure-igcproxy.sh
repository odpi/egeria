#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

# Script to configure Egeria - see below for variables
# used. Primarily intended as part of automated kubernetes/docker setup,
# but can also be used interactively if required.

printf "\nConfiguring IGC for VDC\n\n" 

fail=0

if [ "null" = "$IGCPROXY_ENDPOINT" ] || [ -z $IGCPROXY_ENDPOINT ]
then
	echo "IGCPROXY_ENDPOINT must be supplied. This is URL of the Egeria server that will be configured. An example is 'http://egeria.acme.org:9000'"
	fail=1
fi

if [ "null" = "$KAFKA_ENDPOINT" ] || [ -z $KAFKA_ENDPOINT ]
then
	echo "KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$IGC_KAFKA_ENDPOINT" ] || [ -z $IGC_KAFKA_ENDPOINT ]
then
	echo "IGC_KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'igc-kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$IGC_USER" ] || [ -z $IGC_USER ]
then
	echo "IGC_USER must be supplied. This is user name for the igc api. An example is 'admin'"
	fail=1
fi

if [ "null" = "$EGERIA_USER" ] || [ -z $EGERIA_USER ]
then
	echo "EGERIA_USER must be supplied. This is user name for the igc api. An example is 'admin'"
	fail=1
fi

if [ "null" = "$IGC_PASSWORD" ] || [ -z $IGC_PASSWORD ]
then
	echo "IGC_PASSWORD must be supplied. This is user name for the igc api. An example is 'admin'"
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


echo "IGC Proxy serverL : ${IGCPROXY_ENDPOINT}"
echo "IGC server URL    : ${IGC_ENDPOINT}"
echo "Kafka broker      : ${KAFKA_ENDPOINT}"
echo "IGC Kafka broker  : ${IGC_KAFKA_ENDPOINT}"
echo "Egeria user name  : ${EGERIA_USER}"
echo "Egeria server name: ${EGERIA_SERVER}"
echo "Egeria Cohort     : ${EGERIA_COHORT}"
echo "IGC User          : ${IGC_USER}"

IGC_USERPASS=`echo -n "${IGC_USER}:${IGC_PASSWORD}" | base64`

echo "Checking KAFKA is up"

n=0
   until [ $n -ge 100 ]
   do
      kafkacat -b ${KAFKA_ENDPOINT} -L  && break  # substitute your command here
      n=$[$n+1]
      sleep 30
   done
if [ $? -ne 0 ]
then
	echo "Failed to connect to KAFKA"
	exit 1
fi

echo "Checking IGC KAFKA is up"

n=0
   until [ $n -ge 100 ]
   do
      kafkacat -b ${IGC_KAFKA_ENDPOINT} -L  && break  # substitute your command here
      n=$[$n+1]
      sleep 30
   done
if [ $? -ne 0 ]
then
	echo "Failed to connect to KAFKA"
	exit 1
fi


echo "Checking IGC connector is up"

loop=100
retrytimeout=10
delay=30

# - In a standard environment, return code 1 indicates service is running. However in a k8s environment the 
# service appears to respond even before the backend is up. Thus a better test is to force an error.
# So we will do a post, expecting a 401 back when things are ok (full authentication is required)
while [ $loop -gt 0 ]
do
    http --check-status --ignore-stdin --timeout=${retrytimeout} HEAD ${IGCPROXY_ENDPOINT} &> /dev/null
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
	echo "IGC Proxy server was unavailable. Abandoning configuration. Check endpoint URL"
	exit 1
fi

echo "Checking IGC REST is up"

loop=100
retrytimeout=10
delay=30

while [ $loop -gt 0 ]
do
    http --check-status --ignore-stdin --timeout=${retrytimeout} HEAD ${IGCPROXY_ENDPOINT} &> /dev/null
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
	echo "IGC Proxy server was unavailable. Abandoning configuration. Check endpoint URL"
	exit 1
fi


# Issue requests against Egeria
http --verbose --ignore-stdin \
	--check-status \
	--auth admin:admin \
	POST ${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/event-bus   \
	producer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' \
        consumer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' 
rc=$?
if [ $rc -ne 0 ]
then
	printf "\nIGC proxy setup Failed configuring event bus\n" 
	exit 1
fi


# Server root configuration
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
        POST ${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/server-url-root?url=${IGCPROXY_ENDPOINT}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nIGC proxy  setup Failed configuring root\n"
        exit 1
fi

# IGC - configure events
http --verbose --ignore-stdin \
        --check-status \
        --auth admin:admin \
        POST ${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/local-repository/mode/ibm-igc/details \
	igcBaseURL=${IGC_ENDPOINT} \
        igcAuthorization=${IGC_USERPASS}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nIGC proxy setup Failed configuring IGC connector\n"
        exit 1
fi

# IGC - configure events
http --verbose --ignore-stdin \
        --check-status \
        --auth admin:admin \
        POST "${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/local-repository/event-mapper-details?connectorProvider=org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.IGCOMRSRepositoryEventMapperProvider&eventSource=${IGC_KAFKA_ENDPOINT}"
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nIGC proxy setup Failed configuring IGC event mapper\n"
        exit 1
fi

# Cohort configuration
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
        POST ${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/cohorts/${EGERIA_COHORT}
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nIGC Proxy setup Failed configuring cohort\n"
        exit 1
fi

# Activation - note 5 minute timeout
echo "Configuration complete -- now activating (may take a few minutes)"
http --verbose --ignore-stdin \
	--check-status \
        --auth admin:admin \
	--timeout 900 \
        POST ${IGCPROXY_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/instance
rc=$?
if [ $rc -ne 0 ]
then
        printf "\nIGC Proxy setup Failed starting instance\n"
        exit 1
fi
printf "\nIGC Proxy setup completed\n"

echo "WARNING - Now going into sleep loop - to maintain health successful pod status"
while [ true ]
do
	sleep 100000000
done
exit 0
