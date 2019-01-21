#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.
#echo 'Deleting old DBs'
#echo '---'
#psql -f dropdb.sql1
for file in *sql
do
  echo "Executing $file"
  echo '---'
  psql postgres -f $file
  echo '---'
done
