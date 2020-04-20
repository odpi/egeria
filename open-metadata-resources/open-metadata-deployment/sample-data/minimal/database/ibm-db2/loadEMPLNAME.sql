-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all@
connect to MINIMAL@

BEGIN
    declare continue handler for sqlstate '42710' begin end;

    execute immediate 'CREATE TABLE EMPLNAME'
        || '('
        || '  EMPID INT NOT NULL,'
        || '  FNAME VARCHAR(50) NOT NULL,'
        || '  SURNAME VARCHAR(50) NOT NULL,'
        || '  LOCID INT NOT NULL,'
        || '    PRIMARY KEY(EMPID)'
        || ')';

END@

BEGIN
    declare continue handler for sqlstate '02000' begin end;
    execute immediate 'delete from EMPLNAME';
END@

import from '{{ egeria_samples_minimal_targets.files }}/names.csv' of DEL modified by coldel; skipcount 1 insert into EMPLNAME@

terminate@
