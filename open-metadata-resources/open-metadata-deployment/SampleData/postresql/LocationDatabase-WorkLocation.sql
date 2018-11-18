-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "LocationDatabase"  encoding 'UTF8';

\c "LocationDatabase";

drop table IF EXISTS "WorkLocation" ;

CREATE TABLE "WorkLocation" (
  "WLID" INT NOT NULL,
  "WLName" VARCHAR(40) NOT NULL,
  "ADDR1" VARCHAR(40),
  "ADDR2" VARCHAR(40),
  "ADDR3" VARCHAR(40),
  "ADDR4" VARCHAR(40),
  "ADDR5" VARCHAR(40),
  "ADDR6" VARCHAR(40),
  "ADDR7" VARCHAR(40)


) ;

\copy "WorkLocation"  from '../LocationDatabase-WorkLocation.csv' with csv header DELIMITER ';' ;
