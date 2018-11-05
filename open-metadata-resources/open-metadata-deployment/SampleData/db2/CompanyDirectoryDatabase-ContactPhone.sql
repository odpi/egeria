
disconnect all$
create database CDD$

connect to CDD$


drop table ContactPhone$
CREATE TABLE ContactPhone (
  RecId INT NOT NULL,
  ContactType CHAR NOT NULL,
  Number VARCHAR(40) NOT NULL
) $

import from '../CompanyDirectoryDatabase-ContactPhone.csv' of DEL modified by coldel; skipcount 1 insert into ContactPhone$
terminate$

