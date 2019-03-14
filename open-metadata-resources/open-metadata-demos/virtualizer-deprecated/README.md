<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project.  -->
# Virtualizer
Virtualizer communicates with Information View OMAS and virtualization tool which is currently Gaian.

Virtualizer has three main functions:
1. listen to Information View OMAS Out topic(specified by property information-view-out-topic) and retrieve InformationViewEvent event (json structure);
2. create Business Logical View(business terms are used as column names) and Technical Logical View(source table column names are used as view column names), containing only the columns with business terms assigned
3. create Business View json file and Technical View json file, notify Information View OMAS through publishing on Information View OMAS In topic (specified by property information-view-in-topic).

## Introduction of Gaian
The Gaian is a lightweight dynamically distributed federated database (DDFD) engine based on Apache Derby 10.x. It provides a single centralized view over multiple, heterogeneous back-end data sources using an extensible Logical Table abstraction layer. Detailed information please go to [GitHub/gaian](https://github.com/gaiandb).

Setup for virtualizer impliess a front-end Gaian and one or multiple back-end Gaian nodes which connect all physical databases. Views will be created in the front-end Gaian Node and are linked to Logical Tables from back-end Gaian nodes. Detailed documentation can be found in [ATLAS-1831](https://issues.apache.org/jira/browse/ATLAS-1831).

## Prerequisites

In order for virtualizer to be able to create the views, mappings to the actual database tables need to exist in Gaian
The convention used for naming these mappings is: connectorProviderType + "_" + databaseName + "_" + schemaName + "_" + tableName
Example: ORACLECONNECTOR_XE_HR_EMPSALARYANALYSIS

Commands to create mappings:
call setlt
call setdsrdbtable
Example:
call setlt('ORACLECONNECTOR_XE_HR_EMPLOYEE','DEPT NUMERIC(11,0),EMPSTATUS NUMERIC(11,0),FNAME VARCHAR(40),LNAME VARCHAR(40),LOCCODE NUMERIC(11,0),LVL NUMERIC(11,0),PNUM NUMERIC(11,0),ROLE VARCHAR(40)','')
call setdsrdbtable('ORACLECONNECTOR_XE_HR_EMPLOYEE', '', 'ORACLECONN', 'HR.EMPLOYEE','', 'DEPT,EMPSTATUS,FNAME,LNAME,LOCCODE,LVL,PNUM,ROLE')

Commands to remove mappings: 
call removelt('${tableDefinition}')
Convention used for business/technical table definitions is: type + "_" + gaianNodeName + "_" + connectorProviderType + "_" + databaseName + "_" + schemaName + "_" + tableName
example: LTB_HOST_ORACLECONNECTOR_XE_HR_EMPSALARYANALYSIS







