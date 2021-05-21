<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete processes

Delete a list of Process, with the associated port implementations, port aliases and lineage mappings.
For each port, it will delete the associated schema type and columns.

Check [delete-port-implementation](delete-port-implementation.md), [delete-port-alias](create-port-alias.md) 
and [delete-schema-types](create-schema-type.md) for examples of the more granular payloads.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)
```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes

{
    "qualifiedNames": [
        "(process)=CopyColumsFlow::(process)=CopyColumns",
        "(process)=CopyColumsFlow"
    ],
    "guids": [
        "processGUID"
    ],
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guids` - optional property describing the unique identifiers of the processes to be deleted
`qualifiedNames` - optional property describing the qualifiedNames of the processes to be deleted<br>
All the processes defined in `guids` and `qualifiedNames` will be deleted
`VoidRespone` - void response with status and error message if failing
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







