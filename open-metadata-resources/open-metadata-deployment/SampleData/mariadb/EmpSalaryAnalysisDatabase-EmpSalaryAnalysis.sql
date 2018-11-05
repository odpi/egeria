create database IF NOT EXISTS EmpSalaryAnalysisDatabase  character set = UTF8;

use EmpSalaryAnalysisDatabase;

drop table if exists EmpSalaryAnalysis;
CREATE TABLE EmpSalaryAnalysis (
  HDR INT NOT NULL,
  RECTYPE INT NOT NULL,
  SERVICE INT NOT NULL,
  EMPSTATUS INT NOT NULL,
  FNAME VARCHAR(40) NOT NULL,
  LNAME VARCHAR(40) NOT NULL,
  PNUM INT NOT NULL,
  DEPT INT NOT NULL,
  LVL INT NOT NULL,
  LOCATION VARCHAR(40) NOT NULL,
  LOCCODE INT NOT NULL,
  HOL INT NOT NULL,
  ROLE VARCHAR(40) NOT NULL,
  ETYPE INT NOT NULL,
  SALARY INT NOT NULL,
  BONUS INT NOT NULL,
  SNUM INT NOT NULL,
  STREET VARCHAR(40) NOT NULL,
  CITY VARCHAR(40) NOT NULL,
  STATE VARCHAR(40) NOT NULL,
  TAX INT NOT NULL,
  TAXP INT NOT NULL


) ;

load data infile '/Users/jonesn/VDCData/EmpSalaryAnalysisDatabase-EmpSalaryAnalysis.csv' into table EmpSalaryAnalysis columns terminated by ';' ignore 1 lines;
