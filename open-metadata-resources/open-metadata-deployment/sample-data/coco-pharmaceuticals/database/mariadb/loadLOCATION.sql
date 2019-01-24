-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
use LOCATION;

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

load data infile '{{ egeria_samples_cocopharma_targets.files }}/Location-WorkLocation.csv' into table WORKLOCATION columns terminated by ';' ignore 1 lines;
