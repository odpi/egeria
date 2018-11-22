-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "CompanyDirectoryDatabase"  encoding 'UTF8';

\c "CompanyDirectoryDatabase";

drop table IF EXISTS "ContactPhone" ;
CREATE TABLE "ContactPhone" (
  "RecId" INT NOT NULL,
  "ContactType" CHAR NOT NULL,
  "Number" VARCHAR(40) NOT NULL
) ;

\copy "ContactPhone"  from '../CompanyDirectoryDatabase-ContactPhone.csv' with csv header DELIMITER ';' ;
