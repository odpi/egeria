#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

# Script to configure Atlas for use with egeria - see below for variables
# used. Primarily intended as part of automated kubernetes/docker setup,
# but can also be used interactively if required.

printf "\nConfiguring LDAP for Egeria VDC\n\n" 

fail=0

if [ "null" = "$LDAP_ENDPOINT" ] || [ -z $LDAP_ENDPOINT ]
then
	echo "LDAP_ENDPOINT must be supplied. This is URL of the LDAP server that will be configured. An example is ldap.acme.org:389'"
	fail=1
fi

if [ "null" = "$LDAP_USER" ] || [ -z $LDAP_USER ]
then
	echo "LDAP_USER must be supplied. This is user name for the ldap api 'admin'"
	fail=1
fi

if [ "null" = "$LDAP_PASS" ] || [ -z $LDAP_PASS ]
then
	echo "LDAP_PASS must be supplied. This is the password for the ldap api in Atlas. An example is 'passw0rd'"
	fail=1
fi

LDAP_SERVER=`echo $LDAP_ENDPOINT | cut -d ':' -f1`
LDAP_PORT=`echo $LDAP_ENDPOINT | cut -d ':' -f2`

echo "Checking LDAP is available"

loop=100
retrytimeout=10
delay=30

while [ $loop -gt 0 ]
do
    nc -zv ${LDAP_SERVER} ${LDAP_PORT}
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

# Go to the right location for ldap import files
cd ~/egeria/open-metadata-resources/open-metadata-deployment/SampleData/CocoPharma/ldap
for ldif in *.ldif 
do
  ldapadd -x -w ${LDAP_PASS} -D"${LDAP_USER}" -h ${LDAP_SERVER} -v -c -p $LDAP_PORT -f ${ldif}
done

printf "\nLDAP setup completed\n"

echo "WARNING - Now going into sleep loop - to maintain health successful pod status"
while [ true ]
do
        sleep 100000000
done
exit 0
done
