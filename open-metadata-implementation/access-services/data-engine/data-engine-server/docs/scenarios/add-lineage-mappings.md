<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add lineage mappings

Add LineageMappings between schema types, based on the qualified names of the existing schemas.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/lineage-mappings

{
    "lineageMappings": [
        {
            "sourceAttribute": "column1QualifiedName",
            "targetAttribute": "column2QualifiedName"
        },
        {
             "sourceAttribute": "column1QualifiedName",
             "targetAttribute": "column3QualifiedName"
        }
    ],
    "externalSourceName": "dataEngine"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before adding lineage mappings
`VoidReponse` with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







