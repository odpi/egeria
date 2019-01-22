#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

printf "\nStarting kafkacat to monitor kafka topics...\n\n"

fail=0

if [ "null" = "$KAFKA_ENDPOINT" ] || [ -z $KAFKA_ENDPOINT ]
then
	echo "KAFKA_ENDPOINT must be supplied. This is the address:port of the Kafka Broker. An example is 'kafka.example.org:9092'"
	fail=1
fi

if [ "null" = "$KAFKA_TOPIC" ] || [ -z $KAFKA_TOPIC ]
then
	echo "KAFKA_TOPIC must be supplied. This is the topic to listen to"
	fail=1
fi

if [ $fail -eq 1 ]
then
	exit 1
fi

echo "Kafka broker      : ${KAFKA_ENDPOINT}"
echo "Kafka topic       : ${KAFKA_TOPIC}"


loop=100
retrytimeout=10
delay=30

while [ $loop -gt 0 ]
do
  kafkacat -b $KAFKA_ENDPOINT -t $KAFKA_TOPIC -o beginning -C -c2 -f'\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n'
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





