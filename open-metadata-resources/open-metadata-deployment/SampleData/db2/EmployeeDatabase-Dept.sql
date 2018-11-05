disconnect all$
create database ED$
connect to ED$

drop table Dept$
CREATE TABLE Dept (
  DEPCODE INT NOT NULL,
  DEPNAME VARCHAR(40) NOT NULL,
  MANAGER INT
) $

import from '../EmployeeDatabase-Dept.csv' of DEL modified by coldel; skipcount 1 insert into Dept $
terminate$
