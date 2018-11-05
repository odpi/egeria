#!/bin/sh
for file in *sql
do
  echo "Executing $file"
  mysql -u root < $file
  rc=$?
  if [[ $rc -ne 0 ]]
  then
    echo "Failed...."
    exit 1
  fi
done
