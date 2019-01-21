-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
\c "PATIENT";

CREATE TABLE IF NOT EXISTS "PATIENT" (
  "PatientId" INT NOT NULL,
  "Patient" VARCHAR(40) NOT NULL,
  "LH_Addr_1" VARCHAR(40),
  "LH_Addr_2" VARCHAR(40),
  "LH_Addr_3" VARCHAR(40),
  "LH_Addr_4" VARCHAR(40),
  "LH_Addr_5" VARCHAR(40),
  "Trial" VARCHAR(10) NOT NULL
);

delete from "PATIENT";

\copy "PATIENT" from '{{ egeria_samples_cocopharma_targets.files }}/Patient-Patient.csv' with csv header DELIMITER ';';
