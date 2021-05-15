<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create schema type

Create a SchemaType with SchemaAttributes and relationships

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)
```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/schema-types/

{
    "schema": {
        "qualifiedName": "extern:fr:6c18e03b7e4f44089462e858538bd5d2",
        "displayName": "EMPLOYEE",
        "columns": [
            {
                "qualifiedName": "source-schema-attribute-qualified-name",
                "displayName": "DEPT",
                "elementPosition": 0
            },
            {
                "qualifiedName": "(host_(engine))=engine::(data_connection)=EMPLOYEE_extern:fr:16feb06d8fde48eca2851815b6de99dd::(database_schema)=public::(database_table)=EMPLOYEE::(database_column)=EMPSTATUS",
                "displayName": "EMPSTATUS",
                "elementPosition": 0
            },
            {
                "qualifiedName": "(host_(engine))=engine::(data_connection)=EMPLOYEE_extern:fr:16feb06d8fde48eca2851815b6de99dd::(database_schema)=public::(database_table)=EMPLOYEE::(database_column)=FNAME",
                "displayName": "FNAME",
                "elementPosition": 0
            }
        ]
    },
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any schema type.
`GUIDResponse` - response containing the schema type guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







