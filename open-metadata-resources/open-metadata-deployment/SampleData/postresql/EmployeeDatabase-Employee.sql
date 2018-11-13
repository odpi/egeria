-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "EmployeeDatabase"  encoding 'UTF8';

\c "EmployeeDatabase";

drop table IF EXISTS "Employee" ;
CREATE TABLE "Employee" (
  "PNUM" INT NOT NULL,
  "FNAME" VARCHAR(40) NOT NULL,
  "LNAME" VARCHAR(120) NOT NULL,
  "EMPSTATUS" INT NOT NULL,
  "LVL" INT NOT NULL,
  "DEPT" INT NOT NULL,
  "ROLE" VARCHAR(40) NOT NULL,
  "LOCCODE" INT NOT NULL
) ;

\copy "Employee"  from '../EmployeeDatabase-Employee.csv' with csv header DELIMITER ';' ;
