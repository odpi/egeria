-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "EmployeeDatabase" Encoding 'UTF8';

\c "EmployeeDatabase"  ;

drop table IF EXISTS "Dept" ;
CREATE TABLE "Dept" (
  "DEPCODE" INT NOT NULL,
  "DEPNAME" VARCHAR(40) NOT NULL,
  "MANAGER" INT
) ;

\copy "Dept"  from '../EmployeeDatabase-Dept.csv' csv header DELIMITER ';' ;
