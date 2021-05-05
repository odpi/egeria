<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create port alias

Create a PortAlias, with a PortDelegation relationship to a PortImplementation and a ProcessPort relationship to the process.
The PortAlias type and the delegated PortImplementation type must be the same.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/port-aliases

{
    "processQualifiedName": "processQualifiedName",
    "portAlias": {
        "displayName": "port alias display",
        "qualifiedName": "portAliasQualfiiedName",
        "delegatesTo": "portImplementationQualfiedName",
        "type": "INPUT_PORT"
    },
    "externalSourceName": "dataEngine"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
`processQualifiedName` - qualifiedName of the process that the port will be attached to.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any port.
`GUIDResponse` - response containing the port alias guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







