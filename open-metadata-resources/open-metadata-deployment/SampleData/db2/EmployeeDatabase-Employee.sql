
disconnect all$
create database ED$
connect to ED$

drop table Employee$
CREATE TABLE Employee (
  PNUM INT NOT NULL,
  FNAME VARCHAR(40) NOT NULL,
  LNAME VARCHAR(120) NOT NULL,
  EMPSTATUS INT NOT NULL,
  LVL INT NOT NULL,
  DEPT INT NOT NULL,
  ROLE VARCHAR(40) NOT NULL,
  LOCCODE INT NOT NULL
) $


import from '../EmployeeDatabase-Employee.csv' of DEL modified by coldel; skipcount 1 insert into Employee $
terminate$
