<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete relational table

Delete a RelationalTable with columns and relationships

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-asset_endpoints.postman_collection.json)

```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/relational-tables/

{
    "guid": "schemaGUID",
    "qualifiedName": "(host)=HOST::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME",
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guid` - optional property describing the unique identifier of the schema type to be deleted
`qualifiedName` - optional property describing the qualifiedName of the relational table to be deleted<br>
Note that you must provide either the qualifiedNam,e or the guid of the relational table <br>
`VoidRespone` - void response with status and error message if failing


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







