-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all$
connect to Location$

DROP TABLE WorkLocation$

CREATE TABLE WorkLocation (
  WLID INT NOT NULL,
  WLName VARCHAR(40) NOT NULL,
  ADDR1 VARCHAR(40),
  ADDR2 VARCHAR(40),
  ADDR3 VARCHAR(40),
  ADDR4 VARCHAR(40),
  ADDR5 VARCHAR(40),
  ADDR6 VARCHAR(40),
  ADDR7 VARCHAR(40),
    PRIMARY KEY(WLID)
)$
import from '{{ egeria_samples_cocopharma_targets.files }}/Location-WorkLocation.csv' of DEL modified by coldel; skipcount 1 insert into WorkLocation$

terminate$
