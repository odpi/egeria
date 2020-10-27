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

More payloads examples are available in the [postman samples](../../../docs/samples/OLS.postman_collection.json)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.