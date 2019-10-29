<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create port implementation

Create a Process, with an array of port implementations, port aliases and lineage mappings.
For each port, it creates the associated schema type and relationships.

Check [create-port-implementation](create-port-implementation.md), [create-port-alias](create-port-alias.md) 
and [create-schema-types](create-schema-type.md) for examples of the more granular payloads.

```
POST {{omas-url}}/servers/{{server-id-omas}}/open-metadata/access-services/data-engine/users/{{user-id}}/processes

{
  "processes": [
    {
      "qualifiedName": "processQualfiedName",
      "displayName": "display process",
      "name": "processName",
      "description": "process desc",
      "owner": "owner",
      "ownerType": "USER_ID",
      "latestChange": "latest change done",
      "formula": "whateverformula",
      "zoneMembership": [
        "zone 1"
      ],
      "portImplementations": [
        {
          "qualifiedName": "portQualifiedName",
          "displayName": "port display name",
          "type": "INPUT_PORT",
          "schema": {
            "qualifiedName": "scyemaQualfiedName",
            "displayName": "schema display",
            "author": "author",
            "usage": "usage",
            "encodingStandard": "encoding",
            "versionNumber": "12",
            "columns": [
              {
                "qualifiedName": "column1",
                "displayName": "display1",
                "defaultValue": "test",
                "position": 4,
                "cardinality": "test",
                "defaultOvverride": "test",
                "description": "test",
                "formula": "test",
                "dataType": "INT64"
              },
              {
                "qualifiedName": "column2",
                "displayName": "display2",
                "dataType": "INT64",
                "defaultValue": "",
                "position": 4,
                "description": null,
                "formula": null
              }]
          }
        }],
      "lineageMappings": [
        {
          "sourceAttribute":  "column1",
          "targetAttribute": "column2"
        }
      ],
      "portAliases": []
    }]
 }
```

`GUIDListResponse` - response containing the list of created processes guids and
the list of failed processes guids, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







