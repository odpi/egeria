-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all@
connect to Location@

BEGIN
    declare continue handler for sqlstate '42710' begin end;

    execute immediate 'CREATE TABLE WorkLocation'
        || '('
        || '  WLID INT NOT NULL,'
        || '  WLName VARCHAR(40) NOT NULL,'
        || '  ADDR1 VARCHAR(40),'
        || '  ADDR2 VARCHAR(40),'
        || '  ADDR3 VARCHAR(40),'
        || '  ADDR4 VARCHAR(40),'
        || '  ADDR5 VARCHAR(40),'
        || '  ADDR6 VARCHAR(40),'
        || '  ADDR7 VARCHAR(40),'
        || '    PRIMARY KEY(WLID)'
        || ')';

END@

BEGIN
    declare continue handler for sqlstate '02000' begin end;
    execute immediate 'delete from WorkLocation';
END@

import from '{{ egeria_samples_cocopharma_targets.files }}/Location-WorkLocation.csv' of DEL modified by coldel; skipcount 1 insert into WorkLocation@

terminate@
