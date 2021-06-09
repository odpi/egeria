<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create database

Create a database, with the associated schema type. If no schema is provided, one will be created automatically.
If provided, the fields protocol and networkAddress will trigger the creation of a connection and an endpoint

More examples with all available properties for a database can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-technical-assets.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/databases

{
    "database": {
        "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema",
        "displayName": "MINIMAL-basic-schema",
        "protocol" : "ftp",
        "networkAddress" : "localhost",
        "schema": {
            "qualifiedName": "(host)=HOST::(database)=MINIMAL-basic-schema::(database_schema)=DB2INST1",
            "displayName": "DB2INST1"
        }
    },
    "externalSourceName": "Company::Project::ExternalDataEngine"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process
`GUIDResponse` - response containing the database GUID, with status and error message if failing  
`protocol` - used to create connection and endpoint  
`networkAddress` - used to create connection and endpoint

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







