<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create processes

Create a list of Process, with port implementations, port aliases and lineage mappings.
For each port, it creates the associated schema type and columns.

Check [create-port-implementation](create-port-implementation.md), [create-port-alias](create-port-alias.md) 
and [create-schema-types](create-schema-type.md) for examples of the more granular payloads.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-process_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes

{
    "processes": [
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
            "lineageMappings": [
                {
                    "sourceAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Id",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id"
                },
                {
                    "sourceAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=First",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First"
                },
                {
                    "sourceAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Last",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last"
                },
                {
                    "sourceAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Location",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Location"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=EMPID"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Location",
                    "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=EMPID",
                    "targetAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=emplname.csv::(data_file_record)=emplname::(data_file_field)=EMPID"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME",
                    "targetAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=emplname.csv::(data_file_record)=emplname::(data_file_field)=FNAME"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME",
                    "targetAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=emplname.csv::(data_file_record)=emplname::(data_file_field)=LNAME"
                },
                {
                    "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID",
                    "targetAttribute": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=emplname.csv::(data_file_record)=emplname::(data_file_field)=LOCID"
                }
            ],
            "updateSemantic": "REPLACE",
            "parentProcesses": [
                {
                    "qualifiedName": "(process)=CopyColumsFlow",
                    "containmentType": "OWNED"
                }
            ]
        },
        {
            "qualifiedName": "(process)=CopyColumsFlow",
            "displayName": "CopyColumnsFlow",
            "name": "CopyColumnsFlow",
            "description": "CopyColumnsFlow describes high level process input and output and mappings between (sub)processes (if any).",
            "owner": "Platform User",
            "portAliases": [
                {
                    "displayName": "ReadInputFilePortAlias",
                    "qualifiedName": "(process)=CopyColumsFlow::(port)=ReadInputFilePortAlias",
                    "updateSemantic": "REPLACE",
                    "delegatesTo": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputPort",
                    "type": "INPUT_PORT"
                },
                {
                    "displayName": "WriteOutputFilePortAlias",
                    "qualifiedName": "(process)=CopyColumsFlow::(port)=WriteOutputFilePortAlias",
                    "updateSemantic": "REPLACE",
                    "delegatesTo": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputPort",
                    "type": "OUTPUT_PORT"
                }
            ],
            "lineageMappings": [],
            "updateSemantic": "REPLACE"
        }
    ],
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process.
`GUIDListResponse` - response containing the list of created processes GUIDs and
the list of failed processes GUId, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







