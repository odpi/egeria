create database IF NOT EXISTS CompanyDirectoryDatabase  character set = UTF8;

use CompanyDirectoryDatabase;

drop table if exists ContactEmail;
CREATE TABLE ContactEmail (
  RedIf INT NOT NULL,
  EType CHAR NOT NULL,
  Email VARCHAR(120) NOT NULL
) ;

load data infile '/Users/jonesn/VDCData/CompanyDirectoryDatabase-ContactEmail.csv' into table ContactEmail columns terminated by ';' ignore 1 lines;
