-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "PatientDatabase"  encoding 'UTF8';

\c  "PatientDatabase";

drop table IF EXISTS "PatientTable" ;
CREATE TABLE "PatientTable" (
  "PatientId" INT NOT NULL,
  "Patient" VARCHAR(40) NOT NULL,
  "LH_Addr_1" VARCHAR(40),
  "LH_Addr_2" VARCHAR(40),
  "LH_Addr_3" VARCHAR(40),
  "LH_Addr_4" VARCHAR(40),
  "LH_Addr_5" VARCHAR(40),
  "Trial" VARCHAR(10) NOT NULL

) ;


\copy "PatientTable"  from '../PatientDatabase-PatientTable.csv' with csv header DELIMITER ';' ;
