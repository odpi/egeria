<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create database

Create a database and all the entities inside it, if any. These entities are a database schema and relational tables.
If provided, the fields protocol and networkAddress will trigger the creation of a connection, an endpoint and a relationship
to a proper connector type. A connector type is linked to the connection indicating which implementation the connection uses.
By default, the connector type is a OCF one, but for the moment there is no current implementation for it.
This means none of these three entities will be created (connection, connector type, endpoint).

More examples with all available properties for a database can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-asset_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/databases

{
    "database": {
        "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic",
        "displayName": "MINIMAL-basic",
        "protocol" : "ftp",
        "networkAddress" : "localhost",
        "schema": {
            "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic::(database_schema)=DB2INST1",
            "displayName": "DB2INST1"
        }
    },
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process
`GUIDResponse` - response containing the database GUID, with status and error message if failing  
`protocol` - used to create connection, endpoint and the relationship to the proper connector type
`networkAddress` - used to create connection, endpoint and the relationship to the proper connector type

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







