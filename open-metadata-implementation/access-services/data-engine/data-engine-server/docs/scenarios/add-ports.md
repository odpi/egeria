<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add ports

Add existing ports to processes. Creates the ProcessPort relationship for all ports from the payload.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes/{processGUID}/ports

{
	"ports":["port1QualifiedName", "port2QualifiedName"],
	"externalSourceName": "dataEngine"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before adding ports to processes.
`GUIDResponse` - response containing the process guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







