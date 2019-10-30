<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create schema type

Create a SchemaType with SchemaAttributes and relationships

```
POST {{omas-url}}/servers/{{server-id-omas}}/open-metadata/access-services/data-engine/users/{{user-id}}/schema-types/

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
  }
}
```

`GUIDResponse` - response containing the schema type guid, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







