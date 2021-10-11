<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create database

Create a database schema, linking it to a given database, if any is provided. The database schema can also be marked as incomplete,
which means that it is missing data to be completely described, like the database to which it is linked.

More examples with all available properties for a database can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-asset_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/database-schemas

{
    "databaseQualifiedName": "(host)=HOST::(database)=MINIMAL",
    "databaseSchema": {
        "qualifiedName": "(host)=HOST::(database)=MINIMAL::(database_schema)=DB2INST1",
        "displayName": "DB2INST1",
        "description": "DB2INST1 database schema description",
        "owner": "Administrator IIS",
        "ownerType": "USER_ID",
        "zoneMembership": [
            "default"
        ]
    },
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform",
    "incomplete": false
}
```

`databaseQualifiedName` - name of the database to which the database schema will be linked
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process or entity
`incomplete` - determines if the database schema has all needed information to be described. If the value is `true`,
then the database schema is not connected to a database
<br><br>
`GUIDResponse` - response containing the database GUID, with status and error message if failing 

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







