#!/bin/bash

#  SPDX-License-Identifier: Apache-2.0
#  Copyright Contributors to the ODPi Egeria project. 

/usr/sbin/sshd
service postgresql start

if [ ! -z "${POSTGRESQL_PASSWORD}" ]
then
	su - postgres -c "psql -c \"ALTER ROLE postgres with PASSWORD '$POSTGRESQL_PASSWORD';\""
else
	echo "Warning: No password specified for postgresql"
fi

tail -f /var/log/postgresql/postgresql-10-main.log
