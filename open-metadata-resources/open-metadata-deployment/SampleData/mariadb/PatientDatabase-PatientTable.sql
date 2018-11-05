create database IF NOT EXISTS PatientDatabase  character set = UTF8;

use PatientDatabase;

drop table if exists PatientTable;
CREATE TABLE PatientTable (
  PatientId INT NOT NULL,
  Patient VARCHAR(40) NOT NULL,
  LH_Addr_1 VARCHAR(40),
  LH_Addr_2 VARCHAR(40),
  LH_Addr_3 VARCHAR(40),
  LH_Addr_4 VARCHAR(40),
  LH_Addr_5 VARCHAR(40),
  Trial VARCHAR(10) NOT NULL

) ;

load data infile '/Users/jonesn/VDCData/PatientDatabase-PatientTable.csv' into table PatientTable columns terminated by ';' ignore 1 lines;
