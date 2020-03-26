-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all@
connect to MINIMAL@

BEGIN
    declare continue handler for sqlstate '42710' begin end;

    execute immediate 'CREATE TABLE EMPLDIRECTORY'
        || '('
        || '  EMPID INT NOT NULL,'
        || '  EMPNAME VARCHAR(120) NOT NULL,'
        || '  LOCID INT NOT NULL,'
        || '  LOCNAME VARCHAR(50) NOT NULL'
        || ')';

END@

BEGIN
    declare continue handler for sqlstate '02000' begin end;
    execute immediate 'delete from EMPLDIRECTORY';
END@

terminate@
