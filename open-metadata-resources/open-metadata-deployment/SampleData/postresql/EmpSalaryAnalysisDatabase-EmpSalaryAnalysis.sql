-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "EmpSalaryAnalysisDatabase"  encoding 'UTF8';

\c "EmpSalaryAnalysisDatabase";

drop table IF EXISTS "EmpSalaryAnalysis" ;
CREATE TABLE "EmpSalaryAnalysis" (
  "HDR" INT NOT NULL,
  "RECTYPE" INT NOT NULL,
  "SERVICE" INT NOT NULL,
  "EMPSTATUS" INT NOT NULL,
  "FNAME" VARCHAR(40) NOT NULL,
  "LNAME" VARCHAR(40) NOT NULL,
  "PNUM" INT NOT NULL,
  "DEPT" INT NOT NULL,
  "LVL" INT NOT NULL,
  "LOCATION" VARCHAR(40) NOT NULL,
  "LOCCODE" INT NOT NULL,
  "HOL" INT NOT NULL,
  "ROLE" VARCHAR(40) NOT NULL,
  "ETYPE" INT NOT NULL,
  "SALARY" INT NOT NULL,
  "BONUS" INT NOT NULL,
  "SNUM" INT NOT NULL,
  "STREET" VARCHAR(40) NOT NULL,
  "CITY" VARCHAR(40) NOT NULL,
  "STATE" VARCHAR(40) NOT NULL,
  "TAX" INT NOT NULL,
  "TAXP" INT NOT NULL


) ;

\copy "EmpSalaryAnalysis"  from '../EmpSalaryAnalysisDatabase-EmpSalaryAnalysis.csv' with csv header DELIMITER ';' ;
