#!/bin/sh
echo 'Deleting old DBs'
echo '---'
db2 -td$ -f dropdb.sql1
for file in *sql
do
  echo "Executing $file"
  echo '---'
  db2 -td$ -f $file
  echo '---'
done
