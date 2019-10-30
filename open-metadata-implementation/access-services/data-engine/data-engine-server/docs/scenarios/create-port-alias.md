<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create port implementation

Create a PortAlias, with a PortDelegation relationship to a PortImplementation.
The PortAlias type and the delegated PortImplementation type must be the same.

```
POST {{omas-url}}/servers/{{server-id-omas}}/open-metadata/access-services/data-engine/users/{{user-id}}/port-aliases

{	
	"portAlias": 
	{
		"displayName":"port alias display",
		"qualifiedName": "portAliasQualfiiedName",
		"delegatesTo": "portImplementationQualfiedName",
         "type": "INPUT_PORT"
	}
}
```

`GUIDResponse` - response containing the port alias guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







