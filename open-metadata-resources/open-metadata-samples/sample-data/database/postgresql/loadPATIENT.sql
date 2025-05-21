-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
\c PATIENT;

CREATE TABLE IF NOT EXISTS PATIENT (
  PATIENTID INT NOT NULL,
  PATIENT VARCHAR(40) NOT NULL,
  LH_ADDR_1 VARCHAR(40),
  LH_ADDR_2 VARCHAR(40),
  LH_ADDR_3 VARCHAR(40),
  LH_ADDR_4 VARCHAR(40),
  LH_ADDR_5 VARCHAR(40),
  TRIAL VARCHAR(10) NOT NULL
);

delete from PATIENT;

\copy PATIENT from '{{ egeria_samples_cocopharma_targets.files }}/Patient-Patient.csv' with csv header DELIMITER ';';
