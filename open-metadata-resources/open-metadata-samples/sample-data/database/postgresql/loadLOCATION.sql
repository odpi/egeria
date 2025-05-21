-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
\c LOCATION;

CREATE TABLE IF NOT EXISTS WORKLOCATION (
  WLID INT NOT NULL PRIMARY KEY,
  WLNAME VARCHAR(40) NOT NULL,
  ADDR1 VARCHAR(40),
  ADDR2 VARCHAR(40),
  ADDR3 VARCHAR(40),
  ADDR4 VARCHAR(40),
  ADDR5 VARCHAR(40),
  ADDR6 VARCHAR(40),
  ADDR7 VARCHAR(40)
);

delete from WORKLOCATION;

\copy WORKLOCATION from '{{ egeria_samples_cocopharma_targets.files }}/Location-WorkLocation.csv' with csv header DELIMITER ';';
