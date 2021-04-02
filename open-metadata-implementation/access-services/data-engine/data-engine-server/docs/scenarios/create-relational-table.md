<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create relational table

Create a relational table, with the associated schema type and relational columns, and attach it to a database. 

More examples with all available properties for a table can be found in the 
[sample collection](../../../docs/samples/collections/DataEngine-technical-assets.postman_collection.json) 
 

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/relational-tables

{
   {
       "databaseQualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema",
       "table": {
           "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1::(database_table)=EMPLNAME-basic",
           "displayName": "EMPLNAME-basic",
           "columns": [
               {
                   "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1::(database_table)=EMPLNAME-basic::(database_column)=EMPID",
                   "displayName": "EMPID"
               },
               {
                   "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1::(database_table)=EMPLNAME-basic::(database_column)=FNAME",
                   "displayName": "FNAME"
               },
               {
                   "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1::(database_table)=EMPLNAME-basic::(database_column)=LOCID",
                   "displayName": "LOCID"
               },
               {
                   "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1::(database_table)=EMPLNAME-basic::(database_column)=SURNAME",
                   "displayName": "SURNAME"
               }
           ]
       },
       "externalSourceName": "Company::Project::ExternalDataEngine"
   }
}
```

`databaseQualifiedName` - qualifiedName of the database.
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process.
`GUIDResponse` - response containing the database GUID, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







