<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete processes

Delete a Process, with the associated port implementations, port aliases and data flows.
For each port, it will delete the associated schema type and columns.

Check [delete-port-implementation](delete-port-implementation.md), [delete-port-alias](create-port-alias.md) 
and [delete-schema-types](create-schema-type.md) for examples of the more granular payloads.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-process_endpoints.postman_collection.json)
```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes

{
    "qualifiedName": "(process)=CopyColumsFlow::(process)=CopyColumns",
    "guid": "processGUID",
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guid` - optional property describing the unique identifier of the process to be deleted
`qualifiedName` - optional property describing the qualifiedName of the process to be deleted<br>
Note that you must provide either the qualifiedName or the guid of the port implementation <br>
`VoidRespone` - void response with status and error message if failing
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







