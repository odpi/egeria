-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all$
connect to CompDir$

DROP TABLE ContactList$
DROP TABLE ContactEmail$
DROP TABLE ContactPhone$

CREATE TABLE ContactList (
  RecId INT NOT NULL,
  ContactType CHAR NOT NULL,
  FirstName VARCHAR(50) NOT NULL,
  LastName VARCHAR(50) NOT NULL,
  Company VARCHAR(50) NOT NULL,
  JobTitle VARCHAR(80) NOT NULL,
  WorkLocation INT NOT NULL,
    PRIMARY KEY(RecId)
)$
import from '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactList.csv' of DEL modified by coldel; skipcount 1 insert into ContactList$

CREATE TABLE ContactEmail (
  RedIf INT NOT NULL,
  EType CHAR NOT NULL,
  Email VARCHAR(120) NOT NULL,
    FOREIGN KEY CONTACT (RedIf) REFERENCES ContactList ON DELETE NO ACTION
)$
import from '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactEmail.csv' of DEL modified by coldel; skipcount 1 insert into ContactEmail$

CREATE TABLE ContactPhone (
  RecId INT NOT NULL,
  ContactType CHAR NOT NULL,
  Number VARCHAR(40) NOT NULL,
    FOREIGN KEY CONTACT (RecId) REFERENCES ContactList ON DELETE NO ACTION
)$
import from '{{ egeria_samples_cocopharma_targets.files }}/CompDir-ContactPhone.csv' of DEL modified by coldel; skipcount 1 insert into ContactPhone$

terminate$
