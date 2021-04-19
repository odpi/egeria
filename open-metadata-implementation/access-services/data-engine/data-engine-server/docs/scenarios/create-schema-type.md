<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create schema type

Create a SchemaType with SchemaAttributes and relationships

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/schema-types/

{
  "schema":  {
  "qualifiedName": "schemaQuyalifiedName",
  "displayName": "schema display name",
  "author": "author",
  "usage": "usage",
  "columns": [
    {
      "qualifiedName": "firstColumnQualifiedName",
      "displayName": "display",
      "elementPosition": 0,
      "cardinality": "none",
      "dataType": "INTEGER",
      "encodingStandard": "encoding",
      "versionNumber": "12",
    },
    {
      "qualifiedName": "secondColumnQualifiedName",
      "displayName": "display second",
      "elementPosition": 0,
      "cardinality": "none",
      "dataType": "INTEGER"
    }
   ]
  },
  "externalSourceName": "dataEngine"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any schema type.
`GUIDResponse` - response containing the schema type guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







