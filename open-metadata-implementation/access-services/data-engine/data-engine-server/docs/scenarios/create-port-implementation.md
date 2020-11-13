<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create port implementation

Create a PortImplementation, with associated SchemaType and PortSchema relationship.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/port-implementations

{
  "portImplementation": {
    "portType": "OUTPUT_PORT",
    "displayName": "port display name",
    "qualifiedName": "portImplementationQualifiedName",
    "schema": {
      "qualifiedName": "schemaTypeQualifiedName",
      "displayName": "schema display",
      "author": "author",
      "usage": "usage",
      "encodingStandard": "encoding",
      "versionNumber": "12",
      "columns": [
        {
          "qualifiedName": "columnQualoifiedName",
          "displayName": "name",
          "cardinality": "none",
          "position": "1",
          "type": "STRING"
        }
      ]
    }
  },
  "externalSourceName": "dataEngine"
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any port.
`GUIDResponse` - response containing the port guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







