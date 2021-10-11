<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Find entity

Find a Data Engine related entity, based on provided criteria

More examples on finding an entity can be found in the 
[sample collection](../../../docs/samples/collections/DataEngine-asset_endpoints.postman_collection.json) 
 

```
POST {{base-url}}/servers/{{server-id}}/open-metadata/access-services/data-engine/users/{{user-id}}/find
{
    "identifiers" : {
        "qualifiedName": "(file)=CSVFILE.CSV"
    },
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform",
    "type": "Referenceable"
}
```

`qualifiedName` - entity identifier (mandatory)
`externalSourceName` - qualifiedName of the external data engine tool (optional)
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process   
`type` - type of file to be search (optional - defaults to Referenceable)
`GUIDListResponse` - response containing list of GUIDs, that match find criteria


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







