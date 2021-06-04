<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Classification Search Profile

The performance of programmatically searching for existing entities based on their classification(s).

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for searching for entity instances based on classification, called:

- `findEntitiesByClassification` - searches for entity instances based on specific classifications or properties of
  classifications with specific values

The tests exercise each of the following search variations, but only ever retrieve the first page of results
for each:

- `findEntitiesByClassification` for each classification type with the following variations:
    - based on the classification alone
    - exact match a single value
    - exact match both of two values (when the classification type has multiple properties)
    - exact match either of two values (when the classification type has multiple properties)
    - match neither of two values (when the classification type has multiple properties)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.