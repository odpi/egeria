#!/bin/sh
TMPDIR=/tmp/_mariadbimport-ocopharma_tmp
rm -fr $TMPDIR
mkdir -p $TMPDIR
cp ../*csv $TMPDIR
for file in *sql
do
  echo "Copying data to temp dir"
  echo "Executing $file"
  mysql -u root < $file
  rc=$?
  if [[ $rc -ne 0 ]]
  then
    echo "Failed...."
    exit 1
  fi
done
rm -fr $TMPDIR
