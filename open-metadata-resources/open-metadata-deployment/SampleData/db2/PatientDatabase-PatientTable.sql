disconnect all$
create database PD$
connect to PD$

drop table PatientTable$
CREATE TABLE PatientTable (
  PatientId INT NOT NULL,
  Patient VARCHAR(40) NOT NULL,
  LH_Addr_1 VARCHAR(40),
  LH_Addr_2 VARCHAR(40),
  LH_Addr_3 VARCHAR(40),
  LH_Addr_4 VARCHAR(40),
  LH_Addr_5 VARCHAR(40),
  Trial VARCHAR(10) NOT NULL

) $

import from '../PatientDatabase-PatientTable.csv' of DEL modified by coldel; skipcount 1 insert into PatientTable $
terminate$
