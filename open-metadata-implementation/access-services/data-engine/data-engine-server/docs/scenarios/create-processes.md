<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create processes

Create a list of Process, with port implementations, port aliases and lineage mappings.
For each port, it creates the associated schema type and columns.

Check [create-port-implementation](create-port-implementation.md), [create-port-alias](create-port-alias.md) 
and [create-schema-types](create-schema-type.md) for examples of the more granular payloads.

More examples can be found in the
[sample collection](../../../docs/samples/collections/DE_endpoints.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes

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
    }],
    "externalSourceName": "dataEngine"
 }
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process.
`GUIDListResponse` - response containing the list of created processes GUIDs and
the list of failed processes GUId, with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







