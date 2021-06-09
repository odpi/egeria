<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Register tool

Register an external tool.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/software-server-capabilities

{
	"dataEngine": 
	{
	    "qualifiedName": "qualifiedName",
        "displayName": "DataStage",
        "description": "Data Stage",
        "engineType": "DataEngine",
        "engineVersion": "1",
        "enginePatchLevel": "1",
        "vendor": "ibm",
        "version": "123",
        "source": "source"
	}
}
```

`GUIDResponse` - response containing the external tool guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.