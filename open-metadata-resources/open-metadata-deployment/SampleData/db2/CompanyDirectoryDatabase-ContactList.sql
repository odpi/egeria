
disconnect all$
create database CDD$

connect to CDD$

drop table ContactList$
CREATE TABLE ContactList (
  RecId INT NOT NULL,
  ContactType CHAR NOT NULL,
  FirstName VARCHAR(50) NOT NULL,
  LastName VARCHAR(50) NOT NULL,
  Company VARCHAR(50) NOT NULL,
  JobTitle VARCHAR(80) NOT NULL,
  WorkLocation INT NOT NULL
) $

import from '../CompanyDirectoryDatabase-ContactList.csv' of DEL modified by coldel; skipcount 1 insert into ContactList $
terminate$
