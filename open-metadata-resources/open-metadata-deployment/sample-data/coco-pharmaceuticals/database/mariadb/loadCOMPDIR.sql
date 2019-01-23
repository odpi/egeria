-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
use COMPDIR;

CREATE TABLE IF NOT EXISTS CONTACTLIST (
  RECID INT NOT NULL PRIMARY KEY,
  CONTACTTYPE CHAR NOT NULL,
  FIRSTNAME VARCHAR(50) NOT NULL,
  LASTNAME VARCHAR(50) NOT NULL,
  COMPANY VARCHAR(50) NOT NULL,
  JOBTITLE VARCHAR(80) NOT NULL,
  WORKLOCATION INT NOT NULL
);

CREATE TABLE IF NOT EXISTS CONTACTEMAIL (
  REDIF INT NOT NULL,
  ETYPE CHAR NOT NULL,
  EMAIL VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS CONTACTPHONE (
  RECID INT NOT NULL,
  CONTACTTYPE CHAR NOT NULL,
  NUMBER VARCHAR(40) NOT NULL
);

delete from CONTACTPHONE;
delete from CONTACTEMAIL;
delete from CONTACTLIST;

load data infile '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactList.csv' into table CONTACTLIST columns terminated by ';' ignore 1 lines;
load data infile '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactEmail.csv' into table CONTACTEMAIL columns terminated by ';' ignore 1 lines;
load data infile '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactPhone.csv' into table CONTACTPHONE columns terminated by ';' ignore 1 lines;
