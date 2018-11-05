create database IF NOT EXISTS EmployeeDatabase  character set = UTF8;

use EmployeeDatabase  ;

drop table if exists Dept;
CREATE TABLE Dept (
  DEPCODE INT NOT NULL,
  DEPNAME VARCHAR(40) NOT NULL,
  MANAGER INT
) ;

load data infile '/tmp/_mariadbimport-ocopharma_tmp/EmployeeDatabase-Dept.csv' into table Dept columns terminated by ';' ignore 1 lines ( DEPCODE,DEPNAME,@VMANAGER ) SET MANAGER = nullif(@VMANAGER,0) ;
