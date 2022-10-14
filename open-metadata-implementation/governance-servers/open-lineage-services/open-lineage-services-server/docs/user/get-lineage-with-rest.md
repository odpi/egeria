<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Get Lineage for entity with REST

This REST endpoint allows external tools to get a lineage graph for a specific entity

 ```json
   {
     "class": "LineageQueryParameters",
     "scope": "END_TO_END",
     "displayNameMustContain": "",
     "includeProcesses": true
   }
 ```

The allowed values for `scope` are `END_TO_END, ULTIMATE_SOURCE, ULTIMATE_DESTINATION, SOURCE_AND_DESTINATION, VERTICAL`

# Get available entities types from the lineage graph

The endpoint for the available entities types is /lineage/types.

# Get a limited list of node names based on requested type and search value 

The endpoint for the limited list of node names based on requested type and search value that should be contained in the display name
is /lineage/nodes.

# Search the database for entities matching the filtering criteria. 

The endpoint searches the database for entities that match the input. For the queried node, the type is mandatory, the rest of the properties are optional.

```json
    {
        "queriedNode": {
            "type": "Process",
            "name" : "process-name"
        },
        "relatedNodes": [
            {
                "type": "RelationalTable",
                "name": "table_name"
            }
            
        ]
    }
```


More payloads examples are available in the [postman samples](../../../docs/samples/OLS.postman_collection.json)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.