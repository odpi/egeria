#!/bin/sh

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

printf "\nConfiguring Atlas for Egeria VDC\n\n" 

fail=0

if [ "null" = "$ATLAS_ENDPOINT" ]
then
	echo "ATLAS_ENDPOINT must be supplied. This is URL of the Atlas server that will be configured. An example is 'http://atlas.acme.org:9000'"
	fail=1
fi

if [ "null" = "$KAFKA_ENDPOINT" ]
then
	echo "KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$ATLAS_EGERIAUSER" ]
then
	echo "ATLAS_EGERIAUSER must be supplied. This is user name for the egeria api in Atlas. An example is 'admin'"
	fail=1
fi

if [ "null" = "$ATLAS_EGERIASERVER" ]
then
	echo "ATLAS_EGERIASERVER must be supplied. This is the server name for the egeria api in Atlas.. An example is 'myserver'"
	fail=1
fi

if [ $fail -eq 1 ]
then
	exit 1
fi

# TODO: we should have a valid endpoint now. Check a basic 'ping'

# Isue requests against atlasA
http --verbose --ignore-stdin \
	POST ${ATLAS_ENDPOINT}/open-metadata/admin-services/users/${ATLAS_EGERIAUSER}/servers/${ATLAS_EGERIASERVER}/event-bus   \
	producer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' \
        consumer:='{"bootstrap.servers":"'${KAFKA_ENDPOINT}'"}' 
rc=$?
if [ $rc -ne 0 ]
then
	printf "\nAtlas setup Failed configuring event bus\n" 
	exit 1
fi


printf "\nAtlas setup completed\n"
exit 0
