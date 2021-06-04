<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete schema type

Delete a SchemaType with columns and relationships

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)

```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/schema-types/

{
    "guid": "schemaGUID",
    "qualifiedName": "schemaQualifiedName",
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guid` - optional property describing the unique identifier of the schema type to be deleted
`qualifiedName` - optional property describing the qualifiedName of the schema type to be deleted<br>
Note that you must provide either the qualifiedName or the guid of the schema type <br>
`VoidRespone` - void response with status and error message if failing


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







