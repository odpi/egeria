-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all@
connect to MINIMAL@

BEGIN
    declare continue handler for sqlstate '42710' begin end;

    execute immediate 'CREATE TABLE WORKPLACE'
        || '('
        || '  LOCID INT NOT NULL,'
        || '  LOCNAME VARCHAR(50) NOT NULL,'
        || '    PRIMARY KEY(LOCID)'
        || ')';

END@

BEGIN
    declare continue handler for sqlstate '02000' begin end;
    execute immediate 'delete from WORKPLACE';
END@

import from '{{ egeria_samples_minimal_targets.files }}/locations.csv' of DEL modified by coldel; skipcount 1 insert into WORKPLACE@

terminate@
