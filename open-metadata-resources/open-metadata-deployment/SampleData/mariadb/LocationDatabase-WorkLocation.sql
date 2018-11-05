create database IF NOT EXISTS LocationDatabase  character set = UTF8;

use LocationDatabase;

drop table if exists WorkLocation;

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


) ;

load data infile '/Users/jonesn/VDCData/LocationDatabase-WorkLocation.csv' into table WorkLocation columns terminated by ';' ignore 1 lines;
