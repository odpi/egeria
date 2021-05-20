<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add lineage mappings

Add LineageMappings between schema types, based on the qualified names of the existing schemas.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/lineage-mappings

{
    "lineageMappings": [
        {
            "sourceAttribute": "home/files/names.csv::Id::0",
            "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id"
        },
        {
            "sourceAttribute": "home/files/names.csv::First::1",
            "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First"
        },
        {
            "sourceAttribute": "home/files/names.csv::Last::2",
            "targetAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last"
        },
        {
            "sourceAttribute": "home/files/names.csv::Location::3",
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
            "targetAttribute": "home/files/emplname.csv::EMPID::0"
        },
        {
            "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME",
            "targetAttribute": "home/files/emplname.csv::FNAME::1"
        },
        {
            "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME",
            "targetAttribute": "home/files/emplname.csv::LNAME::2"
        },
        {
            "sourceAttribute": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID",
            "targetAttribute": "home/files/emplname.csv::LOCID::3"
        }
    ],
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before adding lineage mappings
`VoidReponse` with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







