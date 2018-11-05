disconnect all$
create database LD$
connect to LD$

drop table WorkLocation$

CREATE TABLE WorkLocation (
  WLID INT NOT NULL,
  WLName VARCHAR(40) NOT NULL,
  ADDR1 VARCHAR(40),
  ADDR2 VARCHAR(40),
  ADDR3 VARCHAR(40),
  ADDR4 VARCHAR(40),
  ADDR5 VARCHAR(40),
  ADDR6 VARCHAR(40),
  ADDR7 VARCHAR(40)


) $


import from '../LocationDatabase-WorkLocation.csv' of DEL modified by coldel; skipcount 1 insert into WorkLocation $
terminate$
