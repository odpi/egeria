-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all@
connect to Employee@

BEGIN
    declare continue handler for sqlstate '42710' begin end;

    execute immediate 'CREATE TABLE Dept'
        || '('
        || '  DEPCODE INT NOT NULL,'
        || '  DEPNAME VARCHAR(40) NOT NULL,'
        || '  MANAGER INT,'
        || '    PRIMARY KEY(DEPCODE)'
        || ')';

    execute immediate 'CREATE TABLE Employee'
        || '('
        || '  PNUM INT NOT NULL,'
        || '  FNAME VARCHAR(40) NOT NULL,'
        || '  LNAME VARCHAR(120) NOT NULL,'
        || '  EMPSTATUS INT NOT NULL,'
        || '  LVL INT NOT NULL,'
        || '  DEPT INT NOT NULL,'
        || '  ROLE VARCHAR(40) NOT NULL,'
        || '  LOCCODE INT NOT NULL,'
        || '    PRIMARY KEY(PNUM),'
        || '    FOREIGN KEY DEPARTMENT (DEPT) REFERENCES Dept ON DELETE NO ACTION'
        || ')';

END@

BEGIN
    declare continue handler for sqlstate '02000' begin end;
    delete from Employee;
    delete from Dept;
END@

import from '{{ egeria_samples_cocopharma_targets.files }}/Employee-Dept.csv' of DEL modified by coldel; skipcount 1 insert into Dept@
import from '{{ egeria_samples_cocopharma_targets.files }}/Employee-Employee.csv' of DEL modified by coldel; skipcount 1 insert into Employee@

terminate@
