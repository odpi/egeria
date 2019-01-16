-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database "CompanyDirectoryDatabase" encoding 'UTF8';

\c "CompanyDirectoryDatabase";

drop table IF EXISTS "ContactEmail" ;
CREATE TABLE "ContactEmail" (
  "RedIf" INT NOT NULL,
  "EType" CHAR NOT NULL,
  "Email" VARCHAR(120) NOT NULL
) ;

\copy "ContactEmail"  from '../CompanyDirectoryDatabase-ContactEmail.csv' with csv header DELIMITER ';' ;
