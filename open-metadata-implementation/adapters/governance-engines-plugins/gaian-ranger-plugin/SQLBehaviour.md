<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
#SQL Behaviour
**Introduction**

This plugin for Gaian supports:
* Access Control Policies
* Data Masking Policies

**Resources & Tags**

This document will provide some example SQL statements and 
current expected results

Policies can be applied to resources including:
* Schema:
    In this case the action will apply to all tables/columns/rows defined within
    this schema
* Table:
    In this case the action will apply to all columns/rows 
    within this table
* Column: 
    In this case the action will apply to the specified column
    on all affected rows
    
Wildcards can be used on resource names:
* The asterisk '*' denotes any 0 or more characters    


Additionally the plugin supports 'tag' based policies. In this case
the resources affected by a policy are determined by any tags that are
associated with that resource as defined in Atlas.

Currently a tag is associated directly with a resource, but in future this
classification will strictly be a classification & propogate through relationships
- for example a column 'ESAL' is associated to a business term 'employee salary'
which in turn is associated with a classification 'SPI=personal'

For the reminder of the discussion we shall describe the expected
behaviour whenever access is restricted, or a column is masked

**Example Table for these examples**

* Schema is 'GAIANDB'
* Table is 'VEMPLOYEE' (this is actually a derby view created by gaian)
* Columns are all varchars : FIRSTNAME, LASTNAME, SALARY
* Salary will be used as the subject of masking & access control

**Note for all SQL queries**

We would normally expect to do

    select * from EMPLOYEE fetch first 100 rows only

However in order to get accurate access control we MUST compose these queries as

    select * from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

This is due to the way Derby and Gaian interact. If this is not done, policies will
be applied more strictly than required (as if all columns in the source table are referenced) and will not follow the 
behaviour below.

**Access Control**

User has no access to 'SALARY'

    SELECT * from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

An empty result set is returned. ie with Columns shown since the query asked for information
from the SALARY column, but zero rows as we are not permitted to return that data


    SELECT FIRSTNAME,LASTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

Rows are returned as normal since SALARY was not requested

    SELECT FIRSTNAME,SALARY from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

An empty result set as in the first example, as salary was requested

    SELECT FIRSTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE where LASTNAME='JONESN'

Usual results returned as no reference is made to Salary

    SELECT FIRSTNAME,LASTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE where SALARY > '100000'

Empty result set as SALARY was found in the predicate

    SELECT FIRSTNAME,SALARY AS INNOCENTNUMBER from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

;-) No that won't work either!

**Data Masking**

The following kinds of masking are supported
* no mask
* null
* \****

If data type conversion is required which prevents the desired mask being applied
(such as **** on a numeric field) we will apply an appropriate substitution
* Dates -> Jan 1 1970 (epoch)
* Numbers -> 0

In this example the SALARY field will have a masking rule of '****' applied

    SELECT * from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

All rows expected are returned, but all values in the SALARY column are replaced with '***'


    SELECT FIRSTNAME,LASTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

Rows are returned as normal since SALARY was not requested

    SELECT FIRSTNAME,SALARY from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

All rows returned, but ****'s in the SALARY column

    SELECT FIRSTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE where LASTNAME='JONESN'

Usual results returned as no reference is made to Salary

    SELECT FIRSTNAME,LASTNAME from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE where SALARY > '100000'

Empty result set as SALARY was found in the predicate. We can't (yet) push down
a change in predicate for more intelligent masking, so we must fail this query as
the user cannot do anything that could act on masked data

    SELECT FIRSTNAME,SALARY AS INNOCENTNUMBER from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE fetch first 100 rows only

.. Good question! 
Expected behaviour is an empty result set...


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
