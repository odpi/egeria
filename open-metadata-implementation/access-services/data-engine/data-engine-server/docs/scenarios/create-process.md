<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create a process

Create a Process, with port implementations.
For each port, it creates the associated schema type and columns.

Check [create-port-implementation](create-port-implementation.md) 
and [create-schema-types](create-schema-type.md) for examples of the more granular payloads.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-process_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes

{
    "process": 
        {
            "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns",
            "displayName": "CopyColumns",
            "name": "CopyColumns",
            "description": "CopyColumns is (sub)process that describes the low level implementation activities performed by a platform or tool.",
            "owner": "Platform User",
            "portImplementations": [
                {
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
                {
                    "displayName": "EmplnameFileOutputPort",
                    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputPort",
                    "updateSemantic": "REPLACE",
                    "type": "OUTPUT_PORT",
                    "schema": {
                        "displayName": "EmplnameFileOutputSchema",
                        "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema",
                        "author": "Platform User",
                        "columns": [
                            {
                                "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=EMPID",
                                "displayName": "EMPID",
                                "minCardinality": 0,
                                "maxCardinality": 0,
                                "allowsDuplicateValues": false,
                                "orderedValues": false,
                                "position": 0
                            },
                            {
                                "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME",
                                "displayName": "FNAME",
                                "minCardinality": 0,
                                "maxCardinality": 0,
                                "allowsDuplicateValues": false,
                                "orderedValues": false,
                                "position": 0
                            },
                            {
                                "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID",
                                "displayName": "LOCID",
                                "minCardinality": 0,
                                "maxCardinality": 0,
                                "allowsDuplicateValues": false,
                                "orderedValues": false,
                                "position": 0
                            },
                            {
                                "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME",
                                "displayName": "LNAME",
                                "minCardinality": 0,
                                "maxCardinality": 0,
                                "allowsDuplicateValues": false,
                                "orderedValues": false,
                                "position": 0
                            }
                        ]
                    }
                }
            ],
            "updateSemantic": "REPLACE"
        }
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process.
`GUIDResponse` - response containing the unique identifiers of the created process, with status and error message if failing.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







