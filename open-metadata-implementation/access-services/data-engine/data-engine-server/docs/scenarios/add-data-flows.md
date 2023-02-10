<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add data flows

Add DataFlows between any Referenceable, based on the qualified names of existing entities.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-process_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/data-flows

{
    "dataFlows": [
        {
            "dataSupplier": "home/files/names.csv::Id::0",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id"
        },
        {
            "dataSupplier": "home/files/names.csv::First::1",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First"
        },
        {
            "dataSupplier": "home/files/names.csv::Last::2",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last"
        },
        {
            "dataSupplier": "home/files/names.csv::Location::3",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Location"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Id",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=EMPID"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=First",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Last",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=NamesFileInputSchema::(column)=Location",
            "dataConsumer": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=EMPID",
            "dataConsumer": "home/files/emplname.csv::EMPID::0"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=FNAME",
            "dataConsumer": "home/files/emplname.csv::FNAME::1"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LNAME",
            "dataConsumer": "home/files/emplname.csv::LNAME::2"
        },
        {
            "dataSupplier": "(process)=CopyColumsFlow::(process)=CopyColumns::(port)=EmplnameFileOutputSchema::(column)=LOCID",
            "dataConsumer": "home/files/emplname.csv::LOCID::3"
        }
    ],
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before adding data flows
`VoidReponse` with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







