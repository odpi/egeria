-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "CompanyDirectoryDatabase"  encoding 'UTF8';

\c "CompanyDirectoryDatabase";

drop table IF EXISTS "ContactList" ;
CREATE TABLE "ContactList" (
  "RecId" INT NOT NULL,
  "ContactType" CHAR NOT NULL,
  "FirstName" VARCHAR(50) NOT NULL,
  "LastName" VARCHAR(50) NOT NULL,
  "Company" VARCHAR(50) NOT NULL,
  "JobTitle" VARCHAR(80) NOT NULL,
  "WorkLocation" INT NOT NULL
) ;

\copy "ContactList"  from '../CompanyDirectoryDatabase-ContactList.csv' with csv header DELIMITER ';' ;
