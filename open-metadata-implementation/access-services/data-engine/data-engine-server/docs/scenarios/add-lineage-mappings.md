<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add lineage mappings

Add LineageMappings between schema types, based on the qualified names of the existing schemas.

```
POST {{omas-url}}/servers/{{server-id-omas}}/open-metadata/access-services/data-engine/users/{{user-id}}/lineage-mappings

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
    ]
}
```

`VoidReponse` with status and error message if failing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







