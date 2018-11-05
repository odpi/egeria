disconnect all$
create database CDD$ 
connect to CDD$
DROP TABLE ContactEmail$ 
CREATE TABLE ContactEmail (
  RedIf INT NOT NULL,
  EType CHAR NOT NULL,
  Email VARCHAR(120) NOT NULL
) $

import from '../CompanyDirectoryDatabase-ContactEmail.csv' of DEL modified by coldel; skipcount 1 insert into ContactEmail $
terminate$
