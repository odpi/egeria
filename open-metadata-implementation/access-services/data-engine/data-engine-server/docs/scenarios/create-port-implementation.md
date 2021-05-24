<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create port implementation

Create a PortImplementation, with associated SchemaType, PortSchema and ProcessPort relationships. 

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/port-implementations

{
    "processQualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns",
    "portImplementation": {
        "displayName": "NamesFileInputPort",
        "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputPort",
        "updateSemantic": "REPLACE",
        "type": "INPUT_PORT",
        "schema": {
            "displayName": "NamesFileInputSchema",
            "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema",
            "author": "Platform User",
            "columns": [
                {
                    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last",
                    "displayName": "Last",
                    "minCardinality": 0,
                    "maxCardinality": 0,
                    "allowsDuplicateValues": false,
                    "orderedValues": false,
                    "position": 0,
                    "dataType": "VARCHAR"
                },
                {
                    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First",
                    "displayName": "First",
                    "minCardinality": 0,
                    "maxCardinality": 0,
                    "allowsDuplicateValues": false,
                    "orderedValues": false,
                    "position": 1,
                    "dataType": "VARCHAR"
                },
                {
                    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id",
                    "displayName": "Id",
                    "minCardinality": 0,
                    "maxCardinality": 0,
                    "allowsDuplicateValues": false,
                    "orderedValues": false,
                    "position": 2,
                    "dataType": "INTEGER"
                },
                {
                    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Location",
                    "displayName": "Location",
                    "minCardinality": 0,
                    "maxCardinality": 0,
                    "allowsDuplicateValues": false,
                    "orderedValues": false,
                    "position": 3,
                    "dataType": "INTEGER"
                }
            ]
        }
    },
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
`processQualifiedName` - qualifiedName of the process that the port will be attached to.

 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any port.
`GUIDResponse` - response containing the port guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







