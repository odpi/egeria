-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
\c EMPLOYEE;

CREATE TABLE IF NOT EXISTS DEPT (
  DEPCODE INT NOT NULL PRIMARY KEY,
  DEPNAME VARCHAR(40) NOT NULL,
  MANAGER INT
);

CREATE TABLE IF NOT EXISTS EMPLOYEE (
  PNUM INT NOT NULL PRIMARY KEY,
  FNAME VARCHAR(40) NOT NULL,
  LNAME VARCHAR(120) NOT NULL,
  EMPSTATUS INT NOT NULL,
  LVL INT NOT NULL,
  DEPT INT NOT NULL REFERENCES DEPT,
  ROLE VARCHAR(40) NOT NULL,
  LOCCODE INT NOT NULL
);

delete from EMPLOYEE;
delete from DEPT;

\copy DEPT from '{{ egeria_samples_cocopharma_targets.files }}/Employee-Dept.csv' with csv header DELIMITER ';';
\copy EMPLOYEE from '{{ egeria_samples_cocopharma_targets.files }}/Employee-Employee.csv' with csv header DELIMITER ';';
